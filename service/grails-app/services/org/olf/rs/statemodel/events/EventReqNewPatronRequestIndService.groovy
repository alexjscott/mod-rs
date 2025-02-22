package org.olf.rs.statemodel.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.olf.okapi.modules.directory.DirectoryEntry;
import org.olf.okapi.modules.directory.Symbol;
import org.olf.rs.HostLMSService;
import org.olf.rs.PatronRequest;
import org.olf.rs.ProtocolReferenceDataValue;
import org.olf.rs.ReshareActionService;
import org.olf.rs.SharedIndexService
import org.olf.rs.statemodel.AbstractEvent;
import org.olf.rs.statemodel.ActionEventResultQualifier;
import org.olf.rs.statemodel.EventFetchRequestMethod;
import org.olf.rs.statemodel.EventResultDetails;
import org.olf.rs.statemodel.Events;
import org.olf.rs.statemodel.Status;

import com.k_int.web.toolkit.settings.AppSetting;

import groovy.json.JsonSlurper;
import groovy.sql.Sql;

/**
 * This event service takes a new requester patron request and validates it and tries to determine the rota
 * @author Chas
 */
public class EventReqNewPatronRequestIndService extends AbstractEvent {

    HostLMSService hostLMSService;
    // PatronNoticeService patronNoticeService;
    ReshareActionService reshareActionService;
    SharedIndexService sharedIndexService;

    @Override
    String name() {
        return(Events.EVENT_REQUESTER_NEW_PATRON_REQUEST_INDICATION);
    }

    @Override
    EventFetchRequestMethod fetchRequestMethod() {
        return(EventFetchRequestMethod.PAYLOAD_ID);
    }

    // Notify us of a new requester patron request in the database
    //
    // Requests are created with a STATE of IDLE, this handler validates the request and sets the state to VALIDATED, or ERROR
    // Called when a new patron request indication happens - usually
    // New patron requests must have a  request.requestingInstitutionSymbol
    @Override
    EventResultDetails processEvent(PatronRequest request, Map eventData, EventResultDetails eventResultDetails) {
        if (request != null) {
            // Generate a human readabe ID to use
            request.hrid = generateHrid()
            log.debug("set request.hrid to ${request.hrid}");

            // if we do not have a service type set it to loan
            if (request.serviceType == null) {
                request.serviceType = ProtocolReferenceDataValue.lookupServiceType(ProtocolReferenceDataValue.SERVICE_TYPE_LOAN);
            }

            // If we were supplied a pickup location, attempt to resolve it here
            DirectoryEntry pickupLoc;
            if (request.pickupLocationSlug) {
                pickupLoc = DirectoryEntry.findBySlug(request.pickupLocationSlug);
            } else if (request.pickupLocationCode) { // deprecated
                pickupLoc = DirectoryEntry.find("from DirectoryEntry de where de.lmsLocationCode=:code and de.status.value='managed'", [code: request.pickupLocationCode]);
            }

            if (pickupLoc != null) {
                request.resolvedPickupLocation = pickupLoc;
                List pickupSymbols  = pickupLoc?.symbols?.findResults { symbol ->
                    symbol?.priority == 'shipping' ? symbol?.authority?.symbol + ':' + symbol?.symbol : null
                }

                // TODO this deserves a better home
                request.pickupLocation = pickupSymbols.size > 0 ? "${pickupLoc.name} --> ${pickupSymbols [0]}" : pickupLoc.name;
            }

            if (request.requestingInstitutionSymbol != null) {
                // We need to validate the requsting location - and check that we can act as requester for that symbol
                Symbol s = reshareApplicationEventHandlerService.resolveCombinedSymbol(request.requestingInstitutionSymbol);
                if (s != null) {
                    // We do this separately so that an invalid patron does not stop information being appended to the request
                    request.resolvedRequester = s;
                }

                Map lookupPatron = reshareActionService.lookupPatron(request, null);
                if (lookupPatron.callSuccess) {
                    boolean patronValid = lookupPatron.patronValid;

                    // If s != null and patronValid == true then the request has passed validation
                    if (s != null && patronValid) {
                        log.debug("Got request ${request}");
                        log.debug(' -> Request is currently ' + Status.PATRON_REQUEST_IDLE + ' - transition to ' + Status.PATRON_REQUEST_VALIDATED);
                    } else if (s == null) {
                        // An unknown requesting institution symbol is a bigger deal than an invalid patron
                        request.needsAttention = true;
                        log.warn("Unkown requesting institution symbol : ${request.requestingInstitutionSymbol}");
                        eventResultDetails.qualifier = ActionEventResultQualifier.QUALIFIER_NO_INSTITUTION_SYMBOL;
                        eventResultDetails.auditMessage = 'Unknown Requesting Institution Symbol: ' + request.requestingInstitutionSymbol;
                    } else {
                        // If we're here then the requesting institution symbol was fine but the patron is invalid
                        eventResultDetails.qualifier = ActionEventResultQualifier.QUALIFIER_INVALID_PATRON;
                        String errors = (lookupPatron?.problems == null) ? '' : (' (Errors: ' + lookupPatron.problems + ')');
                        String status = lookupPatron?.status == null ? '' : (' (Patron state = ' + lookupPatron.status + ')');
                        eventResultDetails.auditMessage = "Failed to validate patron with id: \"${request.patronIdentifier}\".${status}${errors}".toString();
                        request.needsAttention = true;
                    }
                } else {
                    // unexpected error in Host LMS call
                    request.needsAttention = true;
                    eventResultDetails.qualifier = ActionEventResultQualifier.QUALIFIER_HOST_LMS_CALL_FAILED;
                    eventResultDetails.auditMessage = 'Host LMS integration: lookupPatron call failed. Review configuration and try again or deconfigure host LMS integration in settings. ' + lookupPatron?.problems;
                }
            } else {
                eventResultDetails.qualifier = ActionEventResultQualifier.QUALIFIER_NO_INSTITUTION_SYMBOL;
                request.needsAttention = true;
                eventResultDetails.auditMessage = 'No Requesting Institution Symbol';
            }

            // This is a bit dirty - some clients continue to send request.systemInstanceIdentifier rather than request.bibliographicRecordId
            // If we find we are missing a bib record id but do have a system instance identifier, copy it over. Needs sorting properly post PALCI go live
            if ((request.bibliographicRecordId == null) && (request.systemInstanceIdentifier != null)) {
                request.bibliographicRecordId = request.systemInstanceIdentifier
            }

            if ((request.bibliographicRecordId != null) && (request.bibliographicRecordId.length() > 0)) {
                log.debug('calling fetchSharedIndexRecords');
                List<String> bibRecords = sharedIndexService.getSharedIndexActions().fetchSharedIndexRecords([systemInstanceIdentifier: request.bibliographicRecordId]);
                if (bibRecords?.size() == 1) {
                    request.bibRecord = bibRecords[0];
                    // If our OCLC field isn't set, let's try to set it from our bibrecord
                    if (!request.oclcNumber) {
                        try {
                            JsonSlurper slurper = new JsonSlurper();
                            Object bibJson = slurper.parseText(bibRecords[0]);
                            for (identifier in bibJson.identifiers) {
                                String oclcId = getOCLCId(identifier.value);
                                if (oclcId) {
                                    log.debug("Setting request oclcNumber to ${oclcId}");
                                    request.oclcNumber = oclcId;
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            log.warn("Unable to parse bib json: ${e}");
                        }
                    }
                }
            } else {
                log.debug("No request.bibliographicRecordId : ${request.bibliographicRecordId}");
            }
        } else {
            log.warn("Unable to locate request for ID ${eventData.payload.id} OR state != ${Status.PATRON_REQUEST_IDLE} (${request?.state?.code}) isRequester=${request?.isRequester}");
        }

        return(eventResultDetails);
    }

    private String generateHrid() {
        String result = null;

        AppSetting prefixSetting = AppSetting.findByKey('request_id_prefix');
        log.debug("Got app setting ${prefixSetting} ${prefixSetting?.value} ${prefixSetting?.defValue}");

        String hridPrefix = prefixSetting.value ?: prefixSetting.defValue ?: '';

        // Use this to make sessionFactory.currentSession work as expected
        PatronRequest.withSession { session ->
            log.debug('Generate hrid');
            Sql sql = new Sql(session.connection())
            List queryResult  = sql.rows("select nextval('pr_hrid_seq')");
            log.debug("Query result: ${queryResult }");
            result = hridPrefix + queryResult [0].get('nextval')?.toString();
        }
        return(result);
    }

    private String getOCLCId(String id) {
        Pattern pattern = ~/^(ocn|ocm|on)(\d+)/;
        Matcher matcher = id =~ pattern;
        if (matcher.find()) {
            return matcher.group(2);
        }
        return(null);
    }
}
