package com.k_int.folio.rs.models.ISO18626.RequestAgencyMessage

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.k_int.folio.rs.models.ISO18626.ConfirmationHeader;
import com.k_int.folio.rs.models.ISO18626.ErrorData;
import com.k_int.folio.rs.models.ISO18626.Types.Closed.Action;

@JacksonXmlRootElement(localName="requestingAgencyMessageConfirmation")
public class Confirmation {

	/** The header section */
	ConfirmationHeader confirmationHeader;

	/** The action that was performed */
	Action action;

	/** The Error data if any */
	ErrorData errorData;
	
	public Confirmation() {
	}

	public Confirmation(ConfirmationHeader confirmationHeader, Action action, ErrorData errorData) {
		this.confirmationHeader = confirmationHeader;
		this.action = action;
		this.errorData = errorData;
	}
}
