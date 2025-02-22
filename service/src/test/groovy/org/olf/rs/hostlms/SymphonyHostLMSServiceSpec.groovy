package org.olf.rs.hostlms

import grails.testing.services.ServiceUnitTest
import groovy.json.JsonOutput
import org.olf.rs.hostlms.SymphonyHostLMSService
import spock.lang.Specification

class SymphonyHostLMSServiceSpec extends Specification implements ServiceUnitTest<SymphonyHostLMSService> {
    def 'extractAvailableItemsFrom'() {
        setup:
        def parsedSample = new XmlSlurper().parseText(new File('src/test/resources/zresponsexml/symphony-stanford.xml').text);

        when: 'We extract holdings'
        def result = service.extractAvailableItemsFrom(parsedSample);

        then:
        def resultJson = JsonOutput.toJson(result.first());
        result.size() == 1;
        resultJson == '{"itemId":null,"shelvingLocation":"STACKS","callNumber":"HV6534 .V55 J36 2017","reason":null,"shelvingPreference":null,"preference":null,"location":"SAL3","itemLoanPolicy":"STKS-MONO"}';
    }
}

