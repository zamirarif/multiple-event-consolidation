package com.github.zamirarif.kafka.model;

import java.util.List;

/**
 * This DTO is being used to consolidate all the events and its being pushed to StateStore.
 * 
 * @author zamir.arif
 *
 */
public class StatusConsolidationEvent {

	private String migrationCandidateNumber;

	private String meterPointReference;

	private List<String> migrationStatus = null;
	

	

	public String getMigrationCandidateNumber() {
		return migrationCandidateNumber;
	}

	public void setMigrationCandidateNumber(String migrationCandidateNumber) {
		this.migrationCandidateNumber = migrationCandidateNumber;
	}

	public String getMeterPointReference() {
		return meterPointReference;
	}

	public void setMeterPointReference(String meterPointReference) {
		this.meterPointReference = meterPointReference;
	}

	public List<String> getMigrationStatus() {
		return migrationStatus;
	}

	public void setMigrationStatus(List<String> migrationStatus) {
		this.migrationStatus = migrationStatus;
	}

	@Override
	public String toString() {
		return "StatusConsolidationEvent [migrationCandidateNumber=" + migrationCandidateNumber
				+ ", meterPointReference=" + meterPointReference + ", migrationStatus=" + migrationStatus + "]";
	}

	

	

}
