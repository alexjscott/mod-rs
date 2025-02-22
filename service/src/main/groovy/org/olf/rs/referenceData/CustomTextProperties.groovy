package org.olf.rs.referenceData;

import com.k_int.web.toolkit.custprops.CustomPropertyDefinition;

import groovy.util.logging.Slf4j

@Slf4j
public class CustomTextProperties {

	/**
	 * Ensures a CustomPropertyDefinition exists in the database
	 * @param name the name of the property
	 * @param local is this local or not (default: true)
	 * @param label the label associated with this property (default: null)
	 * @return the CustomPropertyDefinition
	 */
	public CustomPropertyDefinition ensureTextProperty(String name, boolean local = true, String label = null) {
		CustomPropertyDefinition result = CustomPropertyDefinition.findByName(name);
		if (result == null) {
			result = new CustomPropertyDefinition(
                name:name,
                type:com.k_int.web.toolkit.custprops.types.CustomPropertyText.class,
                defaultInternal: local,
                label:label
            );
			result.save(flush:true, failOnError:true);
		}
		return result;
	}

	/**
	 * Loads the settings into the database	
	 */
	public void load() {
		try {
			log.info("Adding custom text properties to the database");

			ensureTextProperty("ILLPreferredNamespaces", false);
			ensureTextProperty("url", false);
			ensureTextProperty("Z3950BaseName", false);
			ensureTextProperty("local_institutionalPatronId", true, "Institutional patron ID");
			ensureTextProperty("ALMA_AGENCY_ID", true, "ALMA Agency ID");
			ensureTextProperty("AdditionalHeaders", false, "Additional Headers");
			ensureTextProperty("policy.ill.InstitutionalLoanToBorrowRatio", false, "ILL Loan To Borrow Ratio");
		  
		} catch ( Exception e ) {
			log.error("Exception thrown while loading custom text properties", e);
		}
	}
	
	public static void loadAll() {
		(new CustomTextProperties()).load();
	}
}
