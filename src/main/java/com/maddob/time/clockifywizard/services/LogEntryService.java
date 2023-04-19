package com.maddob.time.clockifywizard.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.maddob.time.clockifywizard.models.ClockifyLogEntry;

@Service
public class LogEntryService {
	
	private List<ClockifyLogEntry> logEntries = new ArrayList<>();
	
	public void addEntry(final ClockifyLogEntry logEntry) {
		logEntries.add(logEntry);
	}
	
	public List<ClockifyLogEntry> getAllEntries() {
		return logEntries;
	}
	
	public void addAllEntries(List<ClockifyLogEntry> logEntryList) {
		logEntries.addAll(logEntryList);
	}
	
	public void deleteAllEtries() {
		logEntries.clear();
	}
	
	public double getTotalHours() {
		return logEntries.stream().map(l -> l.durationDecimal())
				.reduce(0.0, (a, b) -> a + b);
	}
	
	public int size() {
		return logEntries.size();
	}
}
