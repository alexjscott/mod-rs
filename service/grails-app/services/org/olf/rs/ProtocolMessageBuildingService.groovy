package org.olf.rs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.olf.okapi.modules.directory.Symbol;

class ProtocolMessageBuildingService {

    private static String LAST_SEQUENCE      = 'lastSeq';
    private static String SEQUENCE           = 'seq';
    private static String SEQUENCE_SEPARATOR = ':';
    private static String SEQUENCE_WRAPPER   = '#';

    private static String SEQUENCE_PREFIX      = SEQUENCE_WRAPPER + SEQUENCE + SEQUENCE_SEPARATOR;
    private static String LAST_SEQUENCE_PREFIX = SEQUENCE_WRAPPER + LAST_SEQUENCE + SEQUENCE_SEPARATOR;

    private static String ALL_REGEX           = '(.*)';
    private static String NUMBER_REGEX        = '(\\d+)';
    private static String END_OF_STRING_REGEX = '$'
    private static String SEQUENCE_REGEX      = ALL_REGEX + SEQUENCE_PREFIX + NUMBER_REGEX + SEQUENCE_WRAPPER + END_OF_STRING_REGEX;
    private static String LAST_SEQUENCE_REGEX = ALL_REGEX + LAST_SEQUENCE_PREFIX + NUMBER_REGEX + SEQUENCE_WRAPPER + END_OF_STRING_REGEX;

  /*
   * This method is purely for building out the structure of protocol messages
   * into an eventdata map, before they're handed off to the protocol message
   * service for sending. This service shouldn't do any other work.
   *
  */

  ProtocolMessageService protocolMessageService
  ReshareApplicationEventHandlerService reshareApplicationEventHandlerService
  ReshareActionService reshareActionService

  public Map buildSkeletonMessage(String messageType) {
    Map message = [
      messageType: messageType,
      header:[
        requestingAgencyId:[:],
        supplyingAgencyId:[:]
      ],
    ]

    return message;
  }


  public Map buildRequestMessage(PatronRequest req, boolean appendSequence = true) {
    Map message = buildSkeletonMessage('REQUEST')

    message.header = buildHeader(req, 'REQUEST', req.resolvedRequester, null)

    message.bibliographicInfo = [
      title: req.title,
      requestingInstitutionSymbol: req.requestingInstitutionSymbol,
      author: req.author,
      subtitle: req.subtitle,
      sponsoringBody: req.sponsoringBody,
      volume: req.volume,
      issue: req.issue,
      startPage: req.startPage,
      numberOfPages: req.numberOfPages,
      edition: req.edition,
      issn: req.issn,
      isbn: req.isbn,
      doi: req.doi,
      coden: req.coden,
      sici: req.sici,
      bici: req.bici,
      eissn: req.eissn,
      stitle : req.stitle ,
      part: req.part,
      artnum: req.artnum,
      ssn: req.ssn,
      quarter: req.quarter,
      bibliographicRecordId: req.bibliographicRecordId ?: req.systemInstanceIdentifier,  // Shared index bib record ID (Instance identifier)
      titleOfComponent: req.titleOfComponent,
      authorOfComponent: req.authorOfComponent,
      sponsor: req.sponsor,
      informationSource: req.informationSource,
      supplierUniqueRecordId: null,   // Set later on from rota where we store the supplier id
      bibliographicItemId:[
        [ scheme:'oclc', identifierCode:'oclc', identifierValue: req.oclcNumber ]
      ],
      // These should be removed - they have no business being here as they are not part of the protocol
      // oclcNumber shoud go in bibliographicItemId [ { bibliographicItemIdentifierCode:{scheme:''}, bibliographicItemIdentifier:'VALUE' } ]
      systemInstanceIdentifier: req.systemInstanceIdentifier,
      oclcNumber: req.oclcNumber,

    ]
    message.publicationInfo = [
      publisher: req.publisher,
      publicationType: req.publicationType?.value,
      publicationDate: req.publicationDate,

      //TODO what is this publicationDateOfComponent?
      publicationDateOfComponent: req.publicationDateOfComponent,
      placeOfPublication: req.placeOfPublication
    ]
    message.serviceInfo = [
      //TODO the following fields are permitted here but not currently included:
      /*
       * RequestType
       * RequestSubtype
       * RequestingAgencyPreviousRequestId
       * ServiceLevel
       * PreferredFormat
       * CopyrightCompliance
       * AnyEdition
       * StartDate
       * EndDate
       * Note
      */

      //ToDo the below line currently does nothing since we never actually set serviceType rn
      //serviceType: req.serviceType?.value,

      // ToDo wire in some proper information here instead of this hardcoded stuff
      serviceType: 'Loan',
      serviceLevel: 'Loan',
      anyEdition: 'Y',

      // Note that the internal names sometimes differ from the protocol names--pay attention with these fields
      needBeforeDate: req.neededBy,
      note: buildNote(req, req.patronNote, appendSequence)

    ]
    // TODO SupplierInfo Section

    /* message.supplierInfo = [
     * SortOrder
     * SupplierCode
     * SupplierDescription
     * BibliographicRecordId
     * CallNumber
     * SummaryHoldings
     * AvailabilityNote
    ]
     */
    message.requestedDeliveryInfo = [
      // SortOrder
      address:[
        physicalAddress:[
          line1:req.pickupLocation,
          line2:null,
          locality:null,
          postalCode:null,
          region:null,
          county:null
        ]
      ]
    ]
    // TODO Will this information be taken from the directory entry?
    /* message.requestingAgencyInfo = [
     * Name
     * ContactName
     * Address
    ]
     */
     message.patronInfo = [
      // Note that the internal names differ from the protocol name
      patronId: req.patronIdentifier,
      surname: req.patronSurname,
      givenName: req.patronGivenName,

      patronType: req.patronType,
      //TODO what is this field: patronReference?
      patronReference: req.patronReference,
      /* Also permitted:
       * SendToPatron
       * Address
      */
     ]

     /*
      * message.billingInfo = [
      * Permitted fields:
      * PaymentMethod
      * MaximumCosts
      * BillingMethod
      * BillingName
      * Address
      ]
     */

    return message;
  }

  public Map buildSupplyingAgencyMessage(PatronRequest pr,
                                         String reason_for_message,
                                         String status,
                                         Map messageParams,
                                         boolean appendSequence = true) {

    Map message = buildSkeletonMessage('SUPPLYING_AGENCY_MESSAGE')

    message.header = buildHeader(pr, 'SUPPLYING_AGENCY_MESSAGE', pr.resolvedSupplier, pr.resolvedRequester)
    message.messageInfo = [
      reasonForMessage:reason_for_message,
      note: buildNote(pr, messageParams?.note, appendSequence)
    ]
    message.statusInfo = [
      status:status
    ]

    if ( messageParams.reason ) {
      message.messageInfo.reasonUnfilled = messageParams?.reason
    }

    if ( messageParams.cancelResponse ) {
      if (messageParams.cancelResponse == "yes") {
        message.messageInfo.answerYesNo = "Y"
      } else {
        message.messageInfo.answerYesNo = "N"
      }
    }

    // We need to check in a couple of places whether the note is null/whether to add a note
    String note = messageParams?.note
    message.deliveryInfo = [:]
    if ( messageParams.loanCondition ) {
      message.deliveryInfo['loanCondition'] = messageParams?.loanCondition
      reshareApplicationEventHandlerService.addLoanConditionToRequest(pr, messageParams.loanCondition, pr.resolvedSupplier, note)
    }

    // Whenever a note is attached to the message, create a notification with action.
    if (note != null) {
      Map actionMap = [action: reason_for_message]
      actionMap.status = status

      if (messageParams.loanCondition) {
        actionMap.status = "Conditional"
        actionMap.data = messageParams.loanCondition
      }
      if (messageParams.reason) {
        actionMap.data = messageParams.reason
      }

      reshareActionService.outgoingNotificationEntry(pr, messageParams.note, actionMap, pr.resolvedSupplier, pr.resolvedSupplier, false)
    }

    switch (pr.volumes.size()) {
      case 0:
        break;
      case 1:
        // We have a single volume, send as a single itemId string
        message.deliveryInfo['itemId'] = pr.volumes[0].itemId
        break;
      default:
        // We have many volumes, send as an array of multiVol itemIds
        message.deliveryInfo['itemId'] = pr.volumes.collect { vol -> "multivol:${vol.name},${vol.itemId}" }
        break;
    }

    if( pr?.dueDateRS ) {
      message.statusInfo['dueDate'] = pr.dueDateRS
    }

    return message
  }


  public Map buildRequestingAgencyMessage(PatronRequest pr, String message_sender, String peer, String action, String note, boolean appendSequence = true) {
    Map message = buildSkeletonMessage('REQUESTING_AGENCY_MESSAGE')

    Symbol message_sender_symbol = reshareApplicationEventHandlerService.resolveCombinedSymbol(message_sender)
    Symbol peer_symbol = reshareApplicationEventHandlerService.resolveCombinedSymbol(peer)

    message.header = buildHeader(pr, 'REQUESTING_AGENCY_MESSAGE', message_sender_symbol, peer_symbol)
    message.activeSection = [
      action: action,
      note: buildNote(pr, note, appendSequence)
    ]

    // Whenever a note is attached to the message, create a notification with action.
    if (note != null) {
      Map actionMap = [action: action]
      reshareActionService.outgoingNotificationEntry(
        pr,
        note,
        actionMap,
        message_sender_symbol,
        peer_symbol,
        true
      )
    }
    return message
  }

  /**
   * Extracts the last sequence number from the note field
   * @param note The note that may contain the last sequence
   * @return A map containing the following fields
   *    1. note without the sequence
   *    2. sequence the found sequence
   * if no sequence is found then the sequence will be null
   */
  public Map extractLastSequenceFromNote(String note) {
      return(extractSequence(note, LAST_SEQUENCE_REGEX))
  }

  /**
   * Extracts the sequence number from the note field
   * @param note The note that may contain the last sequence
   * @return A map containing the following fields
   *    1. note without the sequence
   *    2. sequence the found sequence
   * if no sequence is found then the sequence will be null
   */
  public Map extractSequenceFromNote(String note) {
      return(extractSequence(note, SEQUENCE_REGEX))
  }

  /**
   * Builds the last sequence string for our hack to determine if the message was received or not
   * @param request The request we want the last sequence we sent from
   * @return The last sequence sent wrapped in the appropriate format to be set in the note field
   */
  public String buildLastSequence(PatronRequest request) {
      String lastSequenceSent = request.lastSequenceSent == null ? "-1" : request.lastSequenceSent.toString();
      return(LAST_SEQUENCE_PREFIX + lastSequenceSent + SEQUENCE_WRAPPER);
  }

  /**
   * Extracts the sequence number from the note field
   * @param note The note that may contain the last sequence
   * @param sequenceRegex The regex used to obtain the sequence (group 2) and the note (group 1)
   * @return A map containing the following fields
   *    1. note without the sequence
   *    2. sequence the found sequence
   * if no sequence is found then the sequence will be null
   */
  private Map extractSequence(String note, String sequenceRegex) {
      Map result = [ note: note];

      // If we havn't been supplied a note then there is nothing to extract
      if (note != null) {
          // We use Pattern.DOTALL in case there are newlines in the string
          Pattern pattern = Pattern.compile(sequenceRegex, Pattern.DOTALL);
          Matcher matcher = pattern.matcher(note);
          if (matcher.find())
          {
              try {
                  // The sequence matches on the 2nd group
                  String sequenceAsString = matcher.group(2);
                  if (sequenceAsString != null) {
                      // Convert to an integer
                      result.sequence = sequenceAsString.toInteger();

                      // Grab the actual note from the first group as the sequence is always at the end of the note
                      result.note = matcher.group(1);

                      // Need to ensure the note is not blank
                      if (result.note.length() == 0) {
                          // We need to make it null
                          result.note = null;
                      }
                  }
              } catch (Exception ) {
                  // We ignore any exception thrown, as it means it wasn't what we were expecting
              }
          }
      }

      // Return the note and sequence to the caller
      return(result);
  }

  private String buildNote(PatronRequest request, String note, boolean appendSequence) {
      String constructedNote = note;

      // Now do we need to append the sequence
      if (appendSequence) {
          String lastSequence = SEQUENCE_PREFIX + request.incrementLastSequence().toString() + SEQUENCE_WRAPPER;
          if (constructedNote == null) {
              constructedNote = lastSequence;
          } else {
              constructedNote += lastSequence;
          }
      }
      return(constructedNote);
  }

  private Map buildHeader(PatronRequest pr, String messageType, Symbol message_sender_symbol, Symbol peer_symbol) {
    Map supplyingAgencyId
    Map requestingAgencyId
    String requestingAgencyRequestId
    String supplyingAgencyRequestId

    log.debug("ProtocolMessageBuildingService::buildHeader(${pr}, ${messageType}, ${message_sender_symbol}, ${peer_symbol})");


    if (messageType == 'REQUEST' || messageType == 'REQUESTING_AGENCY_MESSAGE') {

      // Set the requestingAgencyId and the requestingAgencyRequestId
      requestingAgencyId = buildHeaderRequestingAgencyId(message_sender_symbol)
      requestingAgencyRequestId = protocolMessageService.buildProtocolId(pr);

      if (messageType == 'REQUEST') {
        // If this message is a request then the supplying Agency details get filled out later and the supplying request id is null
        supplyingAgencyRequestId = null
      } else {
        supplyingAgencyId = buildHeaderSupplyingAgencyId(peer_symbol)
        supplyingAgencyRequestId = pr.peerRequestIdentifier
      }

    } else {
      // Set the AgencyIds
      supplyingAgencyId = buildHeaderSupplyingAgencyId(message_sender_symbol)
      requestingAgencyId = buildHeaderRequestingAgencyId(peer_symbol)

      // Set the RequestIds
      requestingAgencyRequestId = pr.peerRequestIdentifier
      supplyingAgencyRequestId = pr.id
    }

    Map header = [
      supplyingAgencyId: supplyingAgencyId,
      requestingAgencyId: requestingAgencyId,

      requestingAgencyRequestId:requestingAgencyRequestId,
      supplyingAgencyRequestId:supplyingAgencyRequestId
    ]

    return header;
  }

  private Map buildHeaderSupplyingAgencyId(Symbol supplier) {
    Map supplyingAgencyId = [
      agencyIdType: supplier?.authority?.symbol,
      agencyIdValue: supplier?.symbol
    ]
    return supplyingAgencyId;
  }

  private Map buildHeaderRequestingAgencyId(Symbol requester) {
    Map requestingAgencyId = [
      agencyIdType: requester?.authority?.symbol,
      agencyIdValue: requester?.symbol
    ]
    return requestingAgencyId;
  }

}
