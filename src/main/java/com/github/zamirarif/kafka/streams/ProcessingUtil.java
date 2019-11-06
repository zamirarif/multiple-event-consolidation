package com.github.zamirarif.kafka.streams;

import java.util.ArrayList;
import java.util.List;

import com.github.zamirarif.kafka.model.MigrationCandidateEvent;
import com.github.zamirarif.kafka.model.StatusConsolidationEvent;

public class ProcessingUtil {
	private static long lastUpdatedTime;
	
	public long getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(long lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	
	public static boolean updatedWithinLastMillis(long currentTime, long limit) {
		return currentTime - lastUpdatedTime <= limit;
	}
	
	public static StatusConsolidationEvent createRecord(MigrationCandidateEvent migrationCandidateEvent) {

		StatusConsolidationEvent statusConsolidationEvent = new StatusConsolidationEvent();
		statusConsolidationEvent.setMigrationCandidateNumber(migrationCandidateEvent.getMigrationCandidateNumber());
		statusConsolidationEvent.setMeterPointReference(migrationCandidateEvent.getMeterPointReference());
		List<String> migrationStatus = new ArrayList<String>();
		migrationStatus.add(migrationCandidateEvent.getMigrationStatus());
		statusConsolidationEvent.setMigrationStatus(migrationStatus);
		lastUpdatedTime = System.currentTimeMillis();

		System.out.println("New Record : " + statusConsolidationEvent);
		return statusConsolidationEvent;
	}

	public static StatusConsolidationEvent update(StatusConsolidationEvent statusConsolidationEvent,
			MigrationCandidateEvent migrationCandidateEvent) {

		List<String> migrationStatus = new ArrayList<String>();
		migrationStatus = statusConsolidationEvent.getMigrationStatus();
		if (!migrationStatus.contains(migrationCandidateEvent.getMigrationStatus())) {
			migrationStatus.add(migrationCandidateEvent.getMigrationStatus());
			statusConsolidationEvent.setMigrationStatus(migrationStatus);
		}
		lastUpdatedTime = System.currentTimeMillis();
		System.out.println("Update Record : " + statusConsolidationEvent);
		return statusConsolidationEvent;
	}
	
}
