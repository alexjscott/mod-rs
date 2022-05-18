package org.olf.rs.statemodel;

/**
 * Defines the different stages that a status could be in
 */
public enum StatusStage {

	/** Preparing the request */
	PREPARING,

	/** Potentially supplying it locally */
	LOCAL,

	/** It is active with a supplier */
	ACTIVE,

    /** Request has completed */
    COMPLETED
}
