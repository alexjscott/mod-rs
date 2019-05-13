package org.olf.rs.workflow

import grails.gorm.transactions.Transactional;
import org.olf.rs.PatronRequest;
import org.olf.rs.workflow.AbstractAction.ActionResponse;
import groovy.util.logging.Slf4j

@Slf4j
@Transactional
class ActionLocateService extends AbstractAction {

  /** Returns the action that this class represents */
  @Override
  String getActionCode() {
    return(Action.LOCATE);
  }

  /** Performs the action */
  @Override
  ActionResponse perform(PatronRequest requestToBeProcessed) {
    // For the time being we just return OK as we do not do anything
    log.debug("ActionLocateService::perform(${requestToBeProcessed})");

    return(ActionResponse.SUCCESS);
  }
}
