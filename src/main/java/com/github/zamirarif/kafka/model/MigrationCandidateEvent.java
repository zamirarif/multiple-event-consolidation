package com.github.zamirarif.kafka.model;
/**
 * Event which need processing. This is the event we are receiving as source from Kafka which needs further processing. 
 * @author zamir.arif
 *
 */
public class MigrationCandidateEvent {

	String migrationCandidateNumber;
	String meterPointReference;
	String migrationStatus;
	String statusDate;
	String statusTime;
	String reason;

	public MigrationCandidateEvent() {

	}

	public MigrationCandidateEvent(String migrationCandidateNumber, String meterPointReference, String migrationStatus,
			String statusDate, String statusTime, String reason) {
		super();
		this.migrationCandidateNumber = migrationCandidateNumber;
		this.meterPointReference = meterPointReference;
		this.migrationStatus = migrationStatus;
		this.statusDate = statusDate;
		this.statusTime = statusTime;
		this.reason = reason;
	}

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

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}



	@Override
	public String toString() {
		return "MigrationCandidateEvent [migrationCandidateNumber=" + migrationCandidateNumber
				+ ", meterPointReference=" + meterPointReference + ", migrationStatus=" + migrationStatus
				+ ", statusDate=" + statusDate + ", statusTime=" + statusTime + ", reason=" + reason + "]";
	}

}
