package mod.rs;

import org.olf.rs.EmailService
import org.olf.rs.statemodel.AbstractAction;
import org.olf.rs.statemodel.ActionService;
import org.olf.rs.statemodel.Actions;
import org.olf.rs.statemodel.AvailableAction;
import org.olf.rs.statemodel.GraphVizService;
import org.olf.rs.statemodel.StateModel;

import com.k_int.okapi.OkapiTenantAwareController;

import grails.converters.JSON;
import grails.gorm.multitenancy.CurrentTenant;
import groovy.util.logging.Slf4j;

@Slf4j
@CurrentTenant
class AvailableActionController extends OkapiTenantAwareController<AvailableAction>  {

    ActionService actionService;
    GraphVizService graphVizService;

	AvailableActionController() {
		super(AvailableAction)
	}

	/**
	 * Gets hold of the states an action can be called from
	 * Example call: curl --http1.1 -sSLf -H "accept: application/json" -H "Content-type: application/json" -H "X-Okapi-Tenant: diku" --connect-timeout 10 --max-time 30 -XGET http://localhost:8081/rs/availableAction/toStates/Responder/respondYes
	 * @return the array of states the action can be called from
	 */
	def fromStates() {

		def result = [:]
		if (request.method == 'GET') {
			if (params.stateModel && params.actionCode) {
				AbstractAction actionBean = actionService.getServiceAction(params.actionCode, params.stateModel == StateModel.MODEL_REQUESTER);
				if (actionBean == null) {
					result.message = "Can find no class for the action " + params.actionCode + " for the state model " + params.stateModel;
				} else {
					result.fromStates = actionBean.fromStates(params.stateModel);
				}
			} else {
				result.message = "Need to supply both action and state model , to see what states this action could transition from";
			}
		} else {
			request.message("Only GET requests are supported");
		}
		render result as JSON;
    }

	/**
	 * Gets hold of the states an action can be called from
	 * Example call: curl --http1.1 -sSLf -H "accept: application/json" -H "Content-type: application/json" -H "X-Okapi-Tenant: diku" --connect-timeout 10 --max-time 30 -XGET http://localhost:8081/rs/availableAction/fromStates/Responder/respondYes
	 * @return the array of states the action can be called from
	 */
	def toStates() {

		def result = [:]
		if (request.method == 'GET') {
			if (params.stateModel && params.actionCode) {
				AbstractAction actionBean = actionService.getServiceAction(params.actionCode, params.stateModel == StateModel.MODEL_REQUESTER);
				if (actionBean == null) {
					result.message = "Can find no class for the action " + params.actionCode + " for the state model " + params.stateModel;
				} else {
					result.toStates = actionBean.possibleToStates(params.stateModel);
				}
			} else {
				result.message = "Need to supply both action and state model , to see what states this action could transition to";
			}
		} else {
			request.message("Only GET requests are supported");
		}
		render result as JSON;
    }

	/**
	 * Builds a graph of the state models actions
	 * Example call: curl --http1.1 -sSLf -H "accept: image/png" -H "X-Okapi-Tenant: diku" --connect-timeout 10 --max-time 300 -XGET http://localhost:8081/rs/availableAction/createGraph/PatronRequest?height=4000\&excludeActions=requesterCancel,manualClose
	 * @return The png file that is the graph
	 */
	def createGraph() {

		// Remove messagesAllSeen, messageSeen and message as they occur for all states
		// We also only want to keep those for the state model we are interested in
		String nameStartsWith = "action" + params.stateModel.capitalize();
		List<String> ignoredActions = [
            Actions.ACTION_MESSAGES_ALL_SEEN,
            Actions.ACTION_MESSAGE_SEEN,
            Actions.ACTION_MESSAGE,
            Actions.ACTION_INCOMING_ISO18626
        ];
		if (params.excludeActions) {
			// They have specified some additional actions that should be ignored
			ignoredActions.addAll(params.excludeActions.split(","));
		}

		// Send it straight to the output stream
		OutputStream outputStream = response.getOutputStream();

		// Were we passed a height in the parameters
		int height = 2000;
		if (params.height) {
			try {
				height = params.height as int;
			} catch (Exception e) {
			}
		}

        // Do we want to include the protocol actions
        Boolean includeProtocolActions = !((params.excludeProtocolActions == null) ? false : params.excludeProtocolActions.toBoolean());

		// Tell it to build the graph, it should return the dot file in the output stream
		graphVizService.generateGraph(params.stateModel, includeProtocolActions, ignoredActions, outputStream, height);

		// Hopefully we have what we want in the output stream
		outputStream.flush();
		response.status = 200;
		response.setContentType("text/plain");
	}

    /**
     * Tests the jasper reports functionality by outputing the report to D:/Temp/TestPullSlip.pdf
     * Example call: curl --http1.1 -sSLf -H "accept: application/json" -H "X-Okapi-Tenant: diku" --connect-timeout 10 --max-time 300 -XGET http://localhost:8081/rs/availableAction/testReport?id=c2bc1883-5d10-4fb3-ad84-5120f743ffca&id=7a42ed5a-9608-4bef-9ba2-3cc79a377d47
     * @return The png file that is the graph
     */
    org.olf.rs.reporting.JasperReportService jasperReportService;
    EmailService emailService

    def testReport() {
        String schema = "diku_mod_rs";
        List ids;
        if (params.id == null) {
            ids = new ArrayList();
        } else if (params.id instanceof String) {
            ids = new ArrayList();
            ids.add(params.id);
        } else {
            // it must be an array
            ids = params.id;
        }
        String outputFilename = 'D:/Temp/TestPullSlip.pdf';
        org.olf.rs.reports.Report report = org.olf.rs.reports.Report.lookupPredefinedId(org.olf.rs.referenceData.ReportData.ID_PATRON_REQUEST_PULL_SLIP_1);
        OutputStream outputStream = new FileOutputStream(new File(outputFilename));
        try {
            jasperReportService.executeReport(report.id, schema, outputStream, ids);
        } catch (Exception e) {
            log.error("Exception thrown generating report", e);
        } finally {
            outputStream.close();
        }

        // In order to test this ensure you have configured mod-email
        // also need to go through okapi, rather than local otherwise it will not find mod-email
        File file = new File(outputFilename);
        byte[] binaryContent = file.bytes;
        String encoded = binaryContent.encodeBase64().toString();
        Map emailParamaters = [
            notificationId: '1',
            to: 'chaswoodfield@gmail.com',
            header: 'Has the pull slip attached',
            body: 'Will it get through',
            outputFormat: 'text/plain',
            attachments: [
                [
                    contentType: 'application/pdf',
                    name: 'Pull Slip',
                    description: 'This is a Pull Slip',
                    data: encoded,
                    disposition: 'base64'
                ]
            ]
        ];

        Map emailResult = emailService.sendEmail(emailParamaters);
        int chas = 1;
    }
}
