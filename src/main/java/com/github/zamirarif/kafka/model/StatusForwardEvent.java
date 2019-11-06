package com.github.zamirarif.kafka.model;

/**
 * This event we are forwarding once we mark the event consolidation completed. 
 * 
 * This will trigger the event in state store to be deleted. 
 * 
 * @author zamir.arif
 *
 */
public class StatusForwardEvent {

	private String migrationCandidateNumber;

	private String meterPointReference;

	private String migrationStatus;

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

	public String getMigrationStatus() {
		return migrationStatus;
	}

	public void setMigrationStatus(String migrationStatus) {
		this.migrationStatus = migrationStatus;
	}

	@Override
	public String toString() {
		return "StatusForwardEvent [migrationCandidateNumber=" + migrationCandidateNumber + ", meterPointReference="
				+ meterPointReference + ", migrationStatus=" + migrationStatus + "]";
	}
	
	
	
	
	
	
}
