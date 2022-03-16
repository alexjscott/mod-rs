package org.olf.rs

import javax.persistence.LockModeType;

import org.hibernate.LockOptions;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.olf.okapi.modules.directory.DirectoryEntry;
import org.olf.rs.referenceData.Settings;
import org.olf.templating.TemplatingService;

import com.k_int.web.toolkit.refdata.RefdataValue;

import grails.gorm.multitenancy.Tenants;
import groovy.json.JsonBuilder;

/**
 * This service adds generates and processes the notices events
 * @author Chas
 *
 */
public class PatronNoticeService {

    private static final String CATEGORY_DIRECTORY_ENTRY_STATUS = 'DirectoryEntry.Status';
    private static final String CATEGORY_DIRECTORY_ENTRY_TYPE   = 'DirectoryEntry.Type';

    private static final String DIRECTORY_ENTRY_STATUS_MANAGED   = 'Managed';
    private static final String DIRECTORY_ENTRY_TYPE_INSTITUTION = 'Institution';

    EmailService emailService
    TemplatingService templatingService

    public void triggerNotices(PatronRequest pr, RefdataValue trigger) {
        log.debug("triggerNotices(${pr.patronEmail}, ${trigger.value})")

        // The values from the request we are interested in for the notices
        Map values = requestValues(pr);
        triggerNotices(new JsonBuilder(values).toString(), trigger, pr);
    }

    public void triggerNotices(HostLMSPatronProfile hostLMSPatronProfile) {
        // We need to find the email address for the institution

        Map values = [
            email: getAdminEmail(),
            patronProfile: [
                name: hostLMSPatronProfile.name
            ]
        ];

        // There is only one type of notice that can be sent for patron profiles, so it is hard coded here
        triggerNotices(new JsonBuilder(values).toString(), RefdataValue.lookupOrCreate(Settings.VOCABULARY_NOTICE_TRIGGERS, Settings.NOTICE_TRIGGER_NEW_PATRON_PROFILE), null);
    }

    public void triggerNotices(String jsonData, RefdataValue trigger, PatronRequest pr = null) {
        NoticeEvent ne = new NoticeEvent(patronRequest: pr, jsonData: jsonData, trigger: trigger)
        ne.save(flush:true, failOnError:true)
    }

    public void processQueue(String tenant) {
        log.debug("Processing patron notice queue for tenant ${tenant}")
        try {
            Tenants.withId(tenant) {
                NoticeEvent.withSession { sess ->
                    Transaction tx = sess.beginTransaction();
                    // Using SKIP_LOCKED we avoid selecting rows that other timers may be operating on
                    Query query = sess.createQuery('from NoticeEvent where sent=false');
                    query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
                    query.setHint('javax.persistence.lock.timeout', LockOptions.SKIP_LOCKED);
                    query.list().each { ev ->
                        Map values = null;
                        if (ev.jsonData == null) {
                            // This shouldn't happen, it will be for those requests that occurred, just before the upgrade to when we started using jsonData
                            values = requestValues(ev.patronRequest);
                        } else {
                            // The new way of doing it
                            values = new groovy.json.JsonSlurper().parseText(ev.jsonData);
                        }

                        // If we do not have an email address then do not attempt to send it
                        if (values.email != null) {
                            // TODO: incorporate this into the above query
                            NoticePolicyNotice[] notices = NoticePolicyNotice.findAll { noticePolicy.active == true && trigger.id == ev.trigger.id };
                            notices.each { notice ->
                                log.debug("Generating patron notice corresponding to trigger ${ev.trigger.value} for policy ${notice.noticePolicy.name} and template ${notice.template.name}")
                                try {
                                    Map tmplResult = templatingService.performTemplate(notice.template, values, 'en');
                                    Map emailParams = [
                                        notificationId: notice.id,
                                        to: values.email,
                                        header: tmplResult.result.header,
                                        body: tmplResult.result.body,
                                        outputFormat: 'text/html'
                                    ];
                                    emailService.sendEmail(emailParams);
                                } catch (Exception e) {
                                    log.error("Problem sending notice for ${tenant}", e);
                                }
                            }
                        }

                        // "sent" in this case is more like processed -- not all events necessarily result in notices
                        ev.sent = true;
                        ev.save();
                    }

                    // Now we have processed the notices commit the transaction
                    tx.commit();
                }
                NoticeEvent.executeUpdate('delete NoticeEvent ne where ne.sent = true');
            }
        } catch (Exception e) {
            log.error("Problem processing notice triggers for ${tenant}", e);
        } finally {
            log.debug("Completed processing of patron notice triggers for ${tenant}");
        }
    }

    private Map requestValues(PatronRequest pr) {
        // The values from the request we are interested in for the notices
        Map values = [
            email: pr.patronEmail,
            user: [
                id: pr.patronIdentifier,
                givenName: pr?.patronGivenName ?: '',
                surname: pr.patronSurname,
            ],
            request: [
                id: pr.hrid,
                pickupLocation: pr?.pickupLocation ?: '',
                neededBy: pr?.neededBy?.toString() ?: '',
                cancellationReason: pr?.cancellationReason?.label ?: ''
            ],
            item: [
                barcode: pr?.selectedItemBarcode ?: '',
                title: pr.title,
                materialType: pr?.publicationType?.label ?: ''
            ]
        ];
        return(values);
    }

    /**
     * returns the administrators email address for the managed institution
     * If there is more than 1 institution for the tenant, then it just returns the first one it finds for a managed directory entry
     * The institution itself dosn't necessarily need to be managed, as long a one of its children is as it will look up the hierarchy of all managed entries until it finds an institution record
     * @return The administrators email address or null if it does not find one
     */
    private String getAdminEmail() {
        String institutionEmail = null;

        // We need to look the institution directory entry, so find all the managed entries
        RefdataValue refdataManaged = RefdataValue.lookupOrCreate(CATEGORY_DIRECTORY_ENTRY_STATUS, DIRECTORY_ENTRY_STATUS_MANAGED, DIRECTORY_ENTRY_STATUS_MANAGED);
        DirectoryEntry[] allManaged = DirectoryEntry.findAllByStatus(refdataManaged);
        if (allManaged != null) {
            // Good start we have at least 1 managed, so loop through them to find an institution entry
            RefdataValue refdataInstitution = RefdataValue.lookupOrCreate(CATEGORY_DIRECTORY_ENTRY_TYPE, DIRECTORY_ENTRY_TYPE_INSTITUTION, DIRECTORY_ENTRY_TYPE_INSTITUTION);
            allManaged.each { directoryEntry ->
                // Only interested in the first institution we find
                DirectoryEntry currentEntry = directoryEntry;
                while ((currentEntry != null) && (institutionEmail == null)) {
                    if ((currentEntry.type != null) && (refdataInstitution.id == currentEntry.type.id)) {
                        // We have found the institution
                        institutionEmail = currentEntry.emailAddress;
                    } else {
                        // Take a look at the parent
                        currentEntry = currentEntry.parent;
                    }
                }
            }
        }

        // Return the email address of the institution to the caller
        return(institutionEmail);
    }
}
