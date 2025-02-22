package org.olf.rs.statemodel

import grails.gorm.MultiTenant;

/**
 *
 */
class AvailableAction implements MultiTenant<AvailableAction> {

    /** Query which returns all the states than an action may come from */
    private static final String POSSIBLE_FROM_STATES_QUERY = 'select distinct aa.fromState.code from AvailableAction as aa where aa.model.shortcode = :stateModelCode and aa.actionCode = :action and aa.triggerType = :triggerType';
    private static final String POSSIBLE_ACTIONS_FOR_MODEL_QUERY = 'select distinct aa.actionEvent from AvailableAction as aa where aa.model = :model and aa.actionEvent.code not in :excludeActions and aa.triggerType != :excludeActionType';

    public static String TRIGGER_TYPE_MANUAL   = "M"; // Available to users
    public static String TRIGGER_TYPE_PROTOCOL = "P"; // Can occur due to a protocol message
    public static String TRIGGER_TYPE_SYSTEM   = "S";

    String id;
    StateModel model;
    Status fromState;
    String actionCode;  // To be removed once the data has been added

    /** The action / event that is the source for this available action */
    ActionEvent actionEvent;

    // [S]ystem, [M]anual or [P]rotocol
    String triggerType;

    // [S]ervice / [C]losure / [N]one
    String actionType;

    String actionBody;

    /** The default set of results to use for this action / event */
    ActionEventResultList resultList;

    static constraints = {
              model (nullable: false)
          fromState (nullable: false)
        actionEvent (nullable: true, unique: ['model', 'fromState']) // Only temporarily nullable, until the data gets added for it
         actionCode (nullable: false, blank:false) // To be removed once the data has been added
        triggerType (nullable: true, blank:false)
         actionType (nullable: true, blank:false)
         actionBody (nullable: true, blank:false)
         resultList (nullable: true)
    }

    static mapping = {
                 id column : 'aa_id', generator: 'uuid2', length:36
            version column : 'aa_version'
              model column : 'aa_model'
          fromState column : 'aa_from_state'
         actionCode column : 'aa_action_code'  // To be removed once the data has been added
        actionEvent column : 'aa_action_event'
        triggerType column : 'aa_trigger_type'
         actionType column : 'aa_action_type'
         actionBody column : 'aa_action_body'
         resultList column : 'aa_result_list'
    }

    public static AvailableAction ensure(String model, String state, String action, String triggerType, String resultListCode = null) {

        AvailableAction result = null;
        StateModel sm = StateModel.findByShortcode(model);
        if (sm) {
            Status s = Status.lookup(state);
            if (s) {
                result = AvailableAction.findByModelAndFromStateAndActionCode(sm,s,action);
                if (result == null) {
                    // We didn't find it, so create a new one
                    result = new AvailableAction(
                        model: sm,
                        fromState: s,
                        actionCode: action
                    );
                }

                // Update the other fields in case they have changed
                result.actionEvent = ActionEvent.lookup(action);
                result.triggerType = triggerType;
                result.resultList = ActionEventResultList.lookup(resultListCode);
                result.save(flush:true, failOnError:true);
            }
        }
        return result;
    }

	public static String[] getFromStates(String stateModel, String action, String triggerType = TRIGGER_TYPE_MANUAL) {
		return(executeQuery(POSSIBLE_FROM_STATES_QUERY,[stateModelCode: stateModel, action: action, triggerType: triggerType]));
	}

    public static ActionEvent[] getUniqueActionsForModel(StateModel model, List<String> excludeActions, Boolean includeProtocolActions) {
        String notIncludeActionType = includeProtocolActions ? "dummy" : TRIGGER_TYPE_PROTOCOL;
        return(executeQuery(POSSIBLE_ACTIONS_FOR_MODEL_QUERY, [model: model, excludeActions: excludeActions, excludeActionType: notIncludeActionType]));
    }

    public String toString() {
        return "AvailableAction(${id}) ${actionCode} ${triggerType} ${actionType} ${actionBody?.take(40)}".toString()
    }
}
