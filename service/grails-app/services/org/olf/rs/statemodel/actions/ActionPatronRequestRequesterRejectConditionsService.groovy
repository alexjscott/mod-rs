package org.olf.rs.statemodel.actions;

import org.olf.rs.PatronRequest;
import org.olf.rs.statemodel.AbstractAction;
import org.olf.rs.statemodel.ActionResultDetails;
import org.olf.rs.statemodel.StateModel;
import org.olf.rs.statemodel.Status;

public class ActionPatronRequestRequesterRejectConditionsService extends AbstractAction {

	/**
	 * Method that all classes derive from this one that actually performs the action
	 * @param request The request the action is being performed against
	 * @param parameters Any parameters required for the action
	 * @param actionResultDetails The result of performing the action
	 * @return The actionResultDetails 
	 */
	@Override
	ActionResultDetails performAction(PatronRequest request, def parameters, ActionResultDetails actionResultDetails) {

        request.previousStates[Status.PATRON_REQUEST_CANCEL_PENDING] = request.state.code;
        reshareActionService.sendCancel(request, "requesterRejectConditions", parameters);
		actionResultDetails.auditMessage = 'Rejected loan conditions';
		actionResultDetails.newStatus = reshareApplicationEventHandlerService.lookupStatus(StateModel.MODEL_REQUESTER, Status.PATRON_REQUEST_CANCEL_PENDING);

		return(actionResultDetails);
	}
}
