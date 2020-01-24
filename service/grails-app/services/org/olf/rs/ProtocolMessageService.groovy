package org.olf.rs

import grails.gorm.multitenancy.Tenants
import java.util.UUID
import org.olf.okapi.modules.directory.ServiceAccount
import groovy.xml.StreamingMarkupBuilder
import java.text.SimpleDateFormat
import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.ContentTypes.XML
import groovyx.net.http.*


/**
 * Allow callers to request that a protocol message be sent to a remote (Or local) service. Callers
 * provide the requesting and responding symbol and the content of the message, this service works out
 * the most appropriate method/protocol. Initially this will always be loopback.
 *
 */
class ProtocolMessageService {

  ReshareApplicationEventHandlerService reshareApplicationEventHandlerService
  EventPublicationService eventPublicationService
  def grailsApplication

  GlobalConfigService globalConfigService
  /**
   * @param eventData : A map structured as followed 
   *   event: {
   *     envelope:{
   *       sender:{
   *         symbol:''
   *       }
          recipient:{
   *         symbol:''
   *       }
   *       messageType:''
   *       messageBody:{
   *       }
   *     }
   *   }
   *
   * @return a map containing properties including any confirmationId the underlying protocol implementation provides us
   *
   */
  public Map sendProtocolMessage(String message_sender_symbol, String peer_symbol, Map eventData) {

    Map result = [:]

    def responseConfirmed = messageConfirmation(eventData, "request")
    log.debug("sendProtocolMessage called for ${message_sender_symbol}, ${peer_symbol},${eventData}");
    //Make this create a new request in the responder's system
    String confirmation = null;

    assert eventData != null
    assert eventData.messageType != null;
    assert peer_symbol != null;

    // The first thing to do is to look in the internal SharedConfig to see if the recipient is a
    // tenant in this system. If so, we can simply call handleIncomingMessage
    def tenant = globalConfigService.getTenantForSymbol(peer_symbol)
    log.debug("The tenant for that symbol(${peer_symbol}) is: ${tenant}")

    List<ServiceAccount> ill_services_for_peer = findIllServices(peer_symbol)
    log.debug("ILL Services for peer: ${ill_services_for_peer}")


    // If the system can't resolve that symbol, it needs to return a protocol error message -- THIS NEEDS TO BE IN REQUEST CONFIRMATION MESSAGE -- not sure this is being done yet.
    /* if (ill_services_for_peer == null) {
      // TODO add code here to build error data and incorporate in confirmation message
      result.status='ERROR'
    } */

    log.debug("Will send an ISO18626 message to ILL service")

    log.debug("====================================================================")
    log.debug("Event Data: ${eventData}")
    // For now we want to be able to switch between local and actual addresses
    
    def serviceAddress = null;
    if ( ill_services_for_peer.size() > 0 ) {
      serviceAddress = ill_services_for_peer[0].service.address
    }
    else {
      log.warn("Unable to find ILL service address for ${peer_symbol}");
    }

    // THIS IS IMPORTANT - use --isoOverRide="http://localhost:8081/rs/iso18626" to force all request messages onto the
    // loopback address - useful for developers and integration testing 
    if ( grailsApplication.config.getProperty('isoOverRide') != null ) {
      serviceAddress = grailsApplication.config.getProperty('isoOverRide')
      log.warn("isoOverRide IS SET ${serviceAddress}");
    }

    try {
      log.debug("Sending ISO18626 message to symbol ${peer_symbol} - resolved address ${serviceAddress}")
      sendISO18626Message(eventData, serviceAddress)
      result.status = "SENT"
      log.debug("ISO18626 message sent")
    } catch(Exception e) {
      result.status = "NOT SENT"
      log.error("ISO18626 message failed to send.\n ${e.message}",e)
    }
    log.debug("====================================================================")
    
    return result;
  }

  private String mapToEvent(String messageType) {
    String result = null;

    switch ( messageType ) {
      case 'REQUEST':
        result = 'MESSAGE_REQUEST_ind'
        break;
      case 'SUPPLYING_AGENCY_MESSAGE':
        result = 'SUPPLYING_AGENCY_MESSAGE_ind'
        break;
      default:
        log.error("Unhandled event type on incoming protocol message: ${messageType}");
        break;
    }

    assert result != null;

    return result;
  }

  /**
   * @param eventData Symmetrical with the section above. See para on sendProtocolMessage - Map should have exactly the same shape
   * Normally called because a message was received on the wire HOWEVER can be called in a loopback scenario where the peer instition
   * is another tenant in the same re:share installation.
   * eventData should contain a tenantId
   * @return a confirmationId
   */
  public Map handleIncomingMessage(Map eventData) {
    // Recipient must be a tenant in the SharedConfig
    log.debug("handleIncomingMessage called. (eventData.messageType:${eventData.messageType})")
    
    // Now we issue a protcolMessageIndication event so that any handlers written for the protocol message can be 
    // called - this method should not do any work beyond understanding what event needs to be dispatched for the 
    // particular message coming in.
    if (eventData.tenant != null) {
      switch ( eventData.messageType ) {
        case 'REQUEST' :
        case 'SUPPLYING_AGENCY_MESSAGE':
          String topic = "${eventData.tenant}_PatronRequestEvents"
          String key = UUID.randomUUID().toString();
          log.debug("publishEvent(${topic},${key},...");
          eventPublicationService.publishAsJSON(topic, key, eventData)
          break;
        default:
          log.warn("Unhandled message type in eventData : ${eventData}")
          break;
      }
    }
    else {
      log.warn("NO tenant in incoming protocol message - don't know how to route it")
    }
    
    
    
    return [
      confirmationId: UUID.randomUUID().toString()
    ]
  }

  public messageConfirmation(eventData, messageType) {
    //TODO make this able to return a confirmation message if request/supplying agency message/requesting agency message are successful,
    //and returning error messages if not
  }

  /**
   * Return a prioroty order list of service accounts this symbol can accept
   */
  public List<ServiceAccount> findIllServices(String symbol) {
    String[] symbol_components = symbol.split(':');

    log.debug("symbol: ${symbol}, symbol components: ${symbol_components}");
    List<ServiceAccount> result = ServiceAccount.executeQuery('''select sa from ServiceAccount as sa
join sa.accountHolder.symbols as symbol
where symbol.symbol=:sym 
and symbol.authority.symbol=:auth
and sa.service.businessFunction.value=:ill
''', [ ill:'ill', sym:symbol_components[1], auth:symbol_components[0] ] ); 

    log.debug("Got service accounts: ${result}");

    return result;
  }


  def makeISO18626Message(Map eventData) {

    // eventData is expected to have a header with structure:
    /*@param header:[
              supplyingAgencyId: [
                agencyIdType:RESHARE,
                agencyIdValue:VLA
              ],
              requestingAgencyId:[
                agencyIdType:OCLC,
                agencyIdValue:ZMU
              ],
              requestingAgencyRequestId:16,
              supplyingAgencyRequestId:8f41a3a4-daa5-4734-9f4f-32578838ff66]
    */

    log.debug("Creating ISO18626 Message")
    log.debug("Message Type: ${eventData.messageType}")
    return{
      ISO18626Message( 'ill:version':'1.0',
                       'xmlns':'http://illtransactions.org/2013/iso18626',
                       'xmlns:ill': 'http://illtransactions.org/2013/iso18626',
                       'xmlns:xsi': 'http://www.w3.org/2001/XMLSchema-instance',
                       'xsi:schemaLocation': 'http://illtransactions.org/2013/iso18626 http://illtransactions.org/schemas/ISO-18626-v1_1.xsd' ) {
        switch (eventData.messageType) {
          case "REQUEST":
            makeRequestBody(delegate, eventData)
            break;
          case "SUPPLYING_AGENCY_MESSAGE":
            makeSupplyingAgencyMessageBody(delegate, eventData)
            break;
          case "REQUESTING_AGENCY_MESSAGE":
            makeRequestingAgencyMessageBody(delegate, eventData)
            break;
          default:
            log.error("UNHANDLED eventData.messageType : ${eventData.messageType}");
            throw new RuntimeException("UNHANDLED eventData.messageType : ${eventData.messageType}");
        }
      log.debug("ISO18626 message created")
      }
    }
  }

  def sendISO18626Message(Map eventData, String address) {
    StringWriter sw = new StringWriter();
    sw << new StreamingMarkupBuilder().bind (makeISO18626Message(eventData))
    String message = sw.toString();
    log.debug("ISO18626 Message: ${message}")
    def iso18626_response = configure {
      request.uri = address
      request.contentType = XML[0]
      request.headers['accept'] = 'application/xml'
    }.post {
      request.body = message
    }
  }

  void exec ( def del, Closure c ) {
    c.rehydrate(del, c.owner, c.thisObject)()
  } 
  
  void makeRequestBody(def del, eventData) {
    exec(del) {
      request {
        makeHeader(delegate, eventData)

        // Bib info and Service Info only apply to REQUESTS
        log.debug("This is a requesting message, so needs BibliographicInfo")
        if (eventData.bibliographicInfo != null) {
          makeBibliographicInfo(delegate, eventData)
        } else {
          log.warn("No bibliographicInfo found")
        }
        serviceInfo {
          serviceType('Loan')
          serviceLevel('Loan')
          //needBeforeDate('2014-05-01T00:00:00.0Z')
          anyEdition('Y')
        }
      }
    }
  }

  void makeSupplyingAgencyMessageBody(def del, eventData) {
    exec(del) {
      supplyingAgencyMessage {
        makeHeader(delegate, eventData)

        log.debug("This is a supplying agency message, so we need MessageInfo, StatusInfo, DeliveryInfo")
        if (eventData.messageInfo != null) {
          makeMessageInfo(delegate, eventData)
        } else {
          log.warn("No messageInfo found")
        }
        if (eventData.statusInfo != null) {
          makeStatusInfo(delegate, eventData)
        } else {
          log.warn("No statusInfo found")
        }
        if (eventData.deliveryInfo != null) {
          makeDeliveryInfo(delegate, eventData)
        } else {
          log.warn("No deliveryInfo found")
        }
        if (eventData.returnInfo != null) {
          makeReturnInfo(delegate, eventData)
        } else {
          log.warn("No returnInfo found")
        }
      }
    }
  }

  void makeRequestingAgencyMessageBody(def del, eventData) {
    exec(del) {
      requestingAgencyMessage {
        makeHeader(delegate, eventData)

        log.debug("This is a requesting agency message, so we need ActiveSection")
        if (eventData.activeSection != null) {
          makeActiveSection(delegate, eventData)
        } else {
          log.warn("No activeSection found")
        }
      }
    }
  }

  void makeHeader(def del, eventData) {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    exec(del) {
      header {
        supplyingAgencyId {
          agencyIdType(eventData.header.supplyingAgencyId.agencyIdType)
          agencyIdValue(eventData.header.supplyingAgencyId.agencyIdValue)
        }
        requestingAgencyId {
          agencyIdType(eventData.header.requestingAgencyId.agencyIdType)
          agencyIdValue(eventData.header.requestingAgencyId.agencyIdValue)
        }
        timestamp(dateFormatter.format(new Date())) // Current time
        requestingAgencyRequestId(eventData.header.requestingAgencyRequestId)
        if (eventData.messageType == "SUPPLYING_AGENCY_MESSAGE" || eventData.messageType == "REQUESTING_AGENCY_MESSAGE") {
          supplyingAgencyRequestId(eventData.header.supplyingAgencyRequestId)
        }
        if (eventData.messageType == "REQUESTING_AGENCY_MESSAGE") {
          requestingAgencyAuthentication(eventData.header.requestingAgencyAuthentication)
        }
      }
    }
  }

  void makeBibliographicInfo(def del, eventData) {
    exec(del) {
      bibliographicInfo {
        supplier(eventData.bibliographicInfo.supplyingInstitutionSymbol)
        requester(eventData.bibliographicInfo.requestingInstitutionSymbol)
        title(eventData.bibliographicInfo.title)
        subtitle(eventData.bibliographicInfo.subtitle)
        author(eventData.bibliographicInfo.author)
        publicationType(eventData.bibliographicInfo.publicationType)
        sponsoringBody(eventData.bibliographicInfo.sponsoringBody)
        publisher(eventData.bibliographicInfo.publisher)
        placeOfPublication(eventData.bibliographicInfo.placeOfPublication)
        volume(eventData.bibliographicInfo.volume)
        issue(eventData.bibliographicInfo.issue)
        startPage(eventData.bibliographicInfo.startPage)
        numberOfPages(eventData.bibliographicInfo.numberOfPages)
        publicationDate(eventData.bibliographicInfo.publicationDate)
        publicationDateOfComponent(eventData.bibliographicInfo.publicationDateOfComponent)
        edition(eventData.bibliographicInfo.edition)
        issn(eventData.bibliographicInfo.issn)
        isbn(eventData.bibliographicInfo.isbn)
        doi(eventData.bibliographicInfo.doi)
        coden(eventData.bibliographicInfo.coden)
        sici(eventData.bibliographicInfo.sici)
        bici(eventData.bibliographicInfo.bici)
        eissn(eventData.bibliographicInfo.eissn)
        stitle(eventData.bibliographicInfo.stitle)
        part(eventData.bibliographicInfo.part)
        artnum(eventData.bibliographicInfo.artnum)
        ssn(eventData.bibliographicInfo.ssn)
        quarter(eventData.bibliographicInfo.quarter)
        systemInstanceIdentifier(eventData.bibliographicInfo.systemInstanceIdentifier)
        titleOfComponent(eventData.bibliographicInfo.titleOfComponent)
        authorOfComponent(eventData.bibliographicInfo.authorOfComponent)
        sponsor(eventData.bibliographicInfo.sponsor)
        informationSource(eventData.bibliographicInfo.informationSource)
        patronIdentifier(eventData.bibliographicInfo.patronIdentifier)
        patronReference(eventData.bibliographicInfo.patronReference)
        patronSurname(eventData.bibliographicInfo.patronSurname)
        patronGivenName(eventData.bibliographicInfo.patronGivenName)
        patronType(eventData.bibliographicInfo.patronType)
        serviceType(eventData.bibliographicInfo.serviceType)
        requestingAgencyRequestId(eventData.header.requestingAgencyRequestId)
        neededBy(eventData.bibliographicInfo.neededBy)
        patronNote(eventData.bibliographicInfo.patronNote)
      }
    }
  }

  void makeMessageInfo(def del, eventData) {
    exec(del) {
      messageInfo {
        reasonForMessage(eventData.messageInfo.reasonForMessage)
        answerYesNo(eventData.messageInfo.answerYesNo)
        note(eventData.messageInfo.note)
        reasonUnfilled(eventData.messageInfo.reasonUnfilled)
        reasonRetry(eventData.messageInfo.reasonRetry)
        offeredCosts(eventData.messageInfo.offeredCosts)
        retryAfter(eventData.messageInfo.retryAfter)
        retryBefore(eventData.messageInfo.retryBefore)
      }
    }
  }

  void makeActiveSection(def del, eventData) {
    exec(del) {
      activeSection {
        action(eventData.activeSection.action)
        note(eventData.activeSection.note)
      }
    }
  }

  void makeStatusInfo(def del, eventData) {
    exec(del) {
      statusInfo {
        status(eventData.statusInfo.status)
        expectedDeliveryDate(eventData.statusInfo.expectedDeliverydate)
        dueDate(eventData.statusInfo.dueDate)
        lastChange(eventData.statusInfo.lastChange)
      }
    }
  }

  void makeDeliveryInfo(def del, eventData) {
    exec(del) {
      deliveryInfo {
        dateSent(eventData.deliveryInfo.dateSent)
        itemId(eventData.deliveryInfo.itemId)
        sentVia(eventData.deliveryInfo.sentVia)
        sentToPatron(eventData.deliveryInfo.sentToPatron)
        loanCondition(eventData.deliveryInfo.loanCondition)
        deliveredFormat(eventData.deliveryInfo.deliveredFormat)
        deliveryCosts(eventData.deliveryInfo.deliveryCosts)
      }
    }
  }

  void makeReturnInfo(def del, eventData) {
    exec(del) {
      returnInfo {
        returnAgencyId(eventData.returnInfo.returnAgencyId)
        name(eventData.returnInfo.name)
        physicalAddress(eventData.returnInfo.physicalAddress)
      }
    }
  }
}
