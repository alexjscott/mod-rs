package org.olf.rs.statemodel;

public class Events {
    /** What gets prefixed  to the status code to generate the event */
    static public final String STATUS_EVENT_PREFIX = 'STATUS_';

    /** What gets added to the status code to generate the event */
    static public final String STATUS_EVENT_POSTFIX = '_ind';


	static public final String EVENT_MESSAGE_REQUEST_INDICATION                       = "MESSAGE_REQUEST_ind";
	static public final String EVENT_NO_IMPLEMENTATION                                = "Event No Implementation";
    static public final String EVENT_REQUESTER_NEW_PATRON_REQUEST_INDICATION          = "Req_New_Patron_Request_ind";
	static public final String EVENT_REQUESTING_AGENCY_MESSAGE_INDICATION             = "REQUESTING_AGENCY_MESSAGE_ind";
    static public final String EVENT_RESPONDER_NEW_PATRON_REQUEST_INDICATION          = "Resp_New_Patron_Request_ind";
	static public final String EVENT_STATUS_REQ_AWAITING_RETURN_SHIPPING_INDICATION   = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_AWAITING_RETURN_SHIPPING + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_BORROWER_RETURNED_INDICATION          = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_BORROWER_RETURNED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_BORROWING_LIBRARY_RECEIVED_INDICATION = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_BORROWING_LIBRARY_RECEIVED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_CANCEL_PENDING_INDICATION             = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_CANCEL_PENDING + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_CANCELLED_INDICATION                  = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_CANCELLED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_CANCELLED_WITH_SUPPLIER_INDICATION    = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_CANCELLED_WITH_SUPPLIER + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_END_OF_ROTA_INDICATION                = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_END_OF_ROTA + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_REQUEST_SENT_TO_SUPPLIER_INDICATION   = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_REQUEST_SENT_TO_SUPPLIER + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_SHIPPED_INDICATION                    = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_SHIPPED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_SOURCING_ITEM_INDICATION              = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_SOURCING_ITEM + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_SUPPLIER_IDENTIFIED_INDICATION        = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_SUPPLIER_IDENTIFIED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_UNFILLED_INDICATION                   = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_UNFILLED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_REQ_VALIDATED_INDICATION                  = STATUS_EVENT_PREFIX + Status.PATRON_REQUEST_VALIDATED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_RES_CANCEL_REQUEST_RECEIVED_INDICATION    = STATUS_EVENT_PREFIX + Status.RESPONDER_CANCEL_REQUEST_RECEIVED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_RES_CANCELLED_INDICATION                  = STATUS_EVENT_PREFIX + Status.RESPONDER_CANCELLED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_RES_CHECKED_IN_TO_RESHARE_INDICATION      = STATUS_EVENT_PREFIX + Status.RESPONDER_CHECKED_IN_TO_RESHARE + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_RES_IDLE_INDICATION                       = STATUS_EVENT_PREFIX + Status.RESPONDER_IDLE + STATUS_EVENT_POSTFIX;
    static public final String EVENT_STATUS_RESPONDER_ERROR_INDICATION                = STATUS_EVENT_PREFIX + Status.RESPONDER_ERROR + STATUS_EVENT_POSTFIX;
    static public final String EVENT_STATUS_RESPONDER_NOT_SUPPLIED_INDICATION         = STATUS_EVENT_PREFIX + Status.RESPONDER_NOT_SUPPLIED + STATUS_EVENT_POSTFIX;
	static public final String EVENT_STATUS_RES_OVERDUE_INDICATION                    = STATUS_EVENT_PREFIX + Status.RESPONDER_OVERDUE + STATUS_EVENT_POSTFIX;
	static public final String EVENT_SUPPLYING_AGENCY_MESSAGE_INDICATION              = "SUPPLYING_AGENCY_MESSAGE_ind";
}
