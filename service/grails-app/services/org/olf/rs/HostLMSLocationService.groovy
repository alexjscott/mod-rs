package org.olf.rs;

/**
 * Perform any services required by the HostLMSLocation domain
 *
 */
public class HostLMSLocationService {

    PatronNoticeService patronNoticeService;

    /**
     * Given a code and name looks to see if the HostLMSLocation record already exists and if not creates it
     * If it it does exist, it ensures that it is active
     * @param code The code for the location
     * @param name The name for the location
     * @return The record that represents this code and name
     */
    public HostLMSLocation EnsureActive(String code, String name) {
        HostLMSLocation loc = HostLMSLocation.findByCodeOrName(code, name);

        // Did we find a location
        if (loc == null) {
            // We do not so create a new one
            loc = new HostLMSLocation(
                code : code,
                name : name,
                icalRrule :'RRULE:FREQ=MINUTELY;INTERVAL=10;WKST=MO'
            );
            loc.save(flush : true, failOnError : true);

            // We have created a new record, so trigger a notice
            patronNoticeService.triggerNotices(loc);
        }  else if (loc.hidden == true) {
            // It is hidden, so unhide it
            loc.hidden = false;
            loc.save(flush : true, failOnError : true);
        }
        return(loc);
    }
}
