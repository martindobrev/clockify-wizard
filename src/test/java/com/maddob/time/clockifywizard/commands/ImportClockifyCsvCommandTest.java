package com.maddob.time.clockifywizard.commands;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.maddob.time.clockifywizard.services.LogEntryService;

class ImportClockifyCsvCommandTest {
	
	
	LogEntryService logEntryService = new LogEntryService();
	
	private final WizardCommands objectUnderTest = new WizardCommands(logEntryService);

	@Test
	void test() {
		var result = objectUnderTest.importCsv("/home/martin/Downloads/test_report.csv");
		assertNotNull(result);
	}

}
