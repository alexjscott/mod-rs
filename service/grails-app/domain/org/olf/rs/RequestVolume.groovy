package org.olf.rs

import com.k_int.web.toolkit.refdata.Defaults
import com.k_int.web.toolkit.refdata.RefdataCategory
import com.k_int.web.toolkit.refdata.RefdataValue

import grails.gorm.multitenancy.Tenants;
import grails.gorm.MultiTenant

class RequestVolume implements MultiTenant<RequestVolume> {

  String id

  String name
  String itemId

  PatronRequest patronRequest

  Date dateCreated
  Date lastUpdated

  String temporaryItemBarcode

  /* 
    This allows us to check whether each item in turn has succeeded NCIP call
    -----SUPPLIER'S SIDE-----
    awaiting_lms_check_out ─┬─► lms_check_out_complete         ─┬─► awaiting_lms_check_in ─┬─► completed
                            └─► lms_check_out_(no_integration) ─┘                          └─► lms_check_in_(no_integration)

    -----REQUESTER'S SIDE-----    
    awaiting_temporary_item_creation ─┬─► temporary_item_created_in_host_lms
                                      └─► temporary_item_creation_(no_integration)
  */
  @Defaults([
    'Awaiting LMS check out', // Automatic
    'LMS check out complete', // Requires NCIP call
    'LMS check out (no integration)', // NCIP off -- deal with manually
    'Awaiting LMS check in', // Automatic
    'Completed',  // Requires NCIP call
    'LMS check in (no integration)', // NCIP off -- deal with manually

    'Awaiting temporary item creation', // Automatic
    'Temporary item created in host LMS', // Requires NCIP call
    'Temporary item creation (no integration)' // NCIP off -- deal with manually
  ])
  RefdataValue status

  static constraints = {
    itemId (blank: false)
    dateCreated (nullable: true, bindable: false)
    lastUpdated (nullable: true, bindable: false)
  }


  // We need to ensure this is unique at the _very_ least per request -- better unique per reshare
  // Bear in mind that we may have to swap generation in and out depending on user input in future
  String generateTemporaryItemBarcode(def enforceMultivolGeneration = null) {
    String temporaryItemBarcode

    if(patronRequest.isRequester) {

      // For multi volume requests we include itemId to ensure uniqueness
      if (enforceMultivolGeneration || patronRequest.volumes.size() > 1) {

        // We assume last 4 digits of barcode is sufficient for uniqueness...
        // The below will not fail for itemId < 4
        temporaryItemBarcode = "${patronRequest.hrid}-${itemId.drop(itemId.size() - 4)}";
      } else {
        // For requests with only one item, can use the hrid of the request
        temporaryItemBarcode = patronRequest.hrid;
      }

    } else {
      //Use the actual barcode for supply-side requests
      temporaryItemBarcode = itemId;
    }
    
    return temporaryItemBarcode;
  }

  def beforeValidate() {
    if (!temporaryItemBarcode) {
      temporaryItemBarcode = generateTemporaryItemBarcode()
    }
  }
  
  static mapping = {
                       id column : 'rv_id', generator: 'uuid2', length:36
                  version column : 'rv_version'
             dateCreated column : 'rv_date_created'
             lastUpdated column : 'rv_last_updated'
                    name column : 'rv_name'
                  itemId column : 'rv_item_id'
           patronRequest column : 'rv_patron_request_fk'
                  status column : 'rv_status_fk'
    temporaryItemBarcode column: 'rv_temporary_item_barcode'
  }
}
