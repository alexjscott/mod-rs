package org.olf.rs.statemodel.actions;

import org.olf.rs.PatronRequest;
import org.olf.rs.statemodel.ActionResultDetails;
import org.olf.rs.statemodel.StateModel;
import org.olf.rs.statemodel.Status;

public class ActionResponderSupplierRespondToCancelService extends ActionResponderService {

	/**
	 * Method that all classes derive from this one that actually performs the action
	 * @param request The request the action is being performed against
	 * @param parameters Any parameters required for the action
	 * @param actionResultDetails The result of performing the action
	 * @return The actionResultDetails 
	 */
	@Override
	ActionResultDetails performAction(PatronRequest request, def parameters, ActionResultDetails actionResultDetails) {

		// Send the response to the requester
		reshareActionService.sendSupplierCancelResponse(request, parameters);

		// If the cancellation is denied, switch the cancel flag back to false, otherwise send request to complete
		if (parameters?.cancelResponse == "no") {
			// We set the new status, to the saved status
			actionResultDetails.newStatus = reshareApplicationEventHandlerService.lookupStatus(StateModel.MODEL_RESPONDER, request.previousStates[request.state.code]);
			actionResultDetails.auditMessage = 'Cancellation denied';
		} else {
			actionResultDetails.auditMessage = 'Cancellation accepted';
			actionResultDetails.newStatus = reshareApplicationEventHandlerService.lookupStatus(StateModel.MODEL_RESPONDER, Status.RESPONDER_CANCELLED);
		}
		
		// Always clear out the saved state
		request.previousStates[request.state.code] = null;
		
		return(actionResultDetails);
	}
}
