package org.olf.rs.hostlms

import grails.testing.services.ServiceUnitTest
import org.olf.rs.hostlms.AlmaHostLMSService
import spock.lang.Specification
import groovy.json.JsonOutput
import groovy.util.XmlSlurper

class AlmaHostLMSServiceSpec extends Specification implements ServiceUnitTest<AlmaHostLMSService> {
    def 'extractAvailableItemsFrom'() {
        setup:
        def parsedSample = new XmlSlurper().parseText(new File("src/test/resources/zresponsexml/${zResponseFile}").text);

        when: 'We extract holdings'
        def result = service.extractAvailableItemsFrom(parsedSample);

        then:
        def resultJson = JsonOutput.toJson(result);
        resultJson == validResult;

        where:
        zResponseFile | validResult
        'alma-pitt-extra-eresource-record.xml' | '[{"itemId":"31735056082393","shelvingLocation":"stacks","callNumber":"HC79.E5 K685 2007","reason":null,"shelvingPreference":null,"preference":null,"location":"ULS - Thomas Blvd","itemLoanPolicy":null}]'
        'alma-princeton.xml' | '[{"itemId":"32101034358281","shelvingLocation":"stacks: Firestone Library","callNumber":"HG2481 .C42 1997","reason":null,"shelvingPreference":null,"preference":null,"location":"Firestone Library","itemLoanPolicy":"Gen"}]'
        'alma-princeton-notfound.xml' | 'null'
    }
}

