package com.maddob.time.clockifywizard.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.maddob.time.clockifywizard.models.ClockifyLogEntry;
import com.maddob.time.clockifywizard.services.LogEntryService;

@ShellComponent
public class WizardCommands {
	
	private final LogEntryService logEntryService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WizardCommands.class);
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public WizardCommands(final LogEntryService logEntryService) {
		this.logEntryService = logEntryService;
	}
	
	@ShellMethod(key = "import-csv")
	public String importCsv(@ShellOption String file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			List<ClockifyLogEntry> logEntries = new ArrayList<>();
			String line;
		    int counter = 0;
		    while ((line = br.readLine()) != null) {
		    	if (counter > 0) {
		    		String[] values = line.split("\",\"");
		    		String[] valuesWithoutQuotes = Arrays.asList(values).stream().map(v -> v.replaceAll("\"", "")).toArray(String[] ::new);
		    		logEntries.add(new ClockifyLogEntry(valuesWithoutQuotes));
		    	}
		    	counter++;   
		    }
		    logEntryService.addAllEntries(logEntries);

			return "Entries successfully imported: " + counter;
		} catch (IOException e) {
			return "File not found";
		}
	}
	
	@ShellMethod(key = "info")
	public String getInfo() {
		var totalHours = logEntryService.getTotalHours();
		var logEntriesSize = logEntryService.size();
		return String.format("entries imported: %d, total hours: %f ", logEntriesSize, totalHours);
	}

	@ShellMethod(key = "clear-data")
	public String clearData() {
		logEntryService.deleteAllEtries();
		return "Imported data cleared";
	}
	

	@ShellMethod(key = "view-daily")
	public void displayAbacus() {
		Map<LocalDate, List<ClockifyLogEntry>> groups = logEntryService.getAllEntries().stream().collect(Collectors.groupingBy(logEntry -> logEntry.startDate()));
		groups.keySet().stream().sorted().forEach(startDate -> {
			LOGGER.info("Entries for {}", startDate);
			List<ClockifyLogEntry> dailyEntries = groups.get(startDate);
			Map<String, List<ClockifyLogEntry>> dailyEntriesPerProject = dailyEntries.stream().collect(Collectors.groupingBy(le -> le.project()));
			dailyEntriesPerProject.keySet().forEach(project -> {
				List<ClockifyLogEntry> projectLogEntries = dailyEntriesPerProject.get(project);
				Optional<Double> totalHours = projectLogEntries.stream().map(e -> e.durationDecimal()).reduce((a, b) -> a + b);
				LOGGER.info("----> {}, {}", project, totalHours);
			});
		});
	}

	/**
	 * Export log entries as CSV
	 * 
	 * Currently the following fields will be exported - some of them are hardcoded (special template usage)
	 * 
	 * Feel free to modify the exported data as you wish :)
	 * 
	 * Current columns:
	 * 
	 * 1: Task (hardcoded)
	 * 2: Story-key (Jira ticket number)
	 * 3: Story name (Description of the task, ticket name)
	 * 4: EJPD FiSCo (hardcoded) 
	 * 5: Entwicklung (hardcoded)
	 * 6: Date (in format dd.MM.yyyy)
	 * 7: Dobrev Martin (hardcoded)
	 * 8: Time spent (decimal with , separation)
	 * 
	 * 
	 * @param file file to write the data to
	 * @param project - filter by project name
	 */
	@ShellMethod(key = "export")
	public void export(@ShellOption String file, @ShellOption(defaultValue = "") String project) {
		List<String[]> logEntries = logEntryService.getAllEntries().stream().filter(logEntry -> {
			if (!project.isEmpty()) {
				return project.equals(logEntry.project());
			}
			return true;
		}).map(le -> new String[] {
			"Task", 
			le.getJiraTicketNumber(), 
			le.description(),
			"EJPD FiSCo",
			"Entwicklung", 
			le.startDate().format(dateFormatter),
			"Dobrev Martin",
			NumberFormat.getInstance(Locale.FRANCE).format(le.durationDecimal()),
			//String.valueOf(le.durationDecimal()),
		}).collect(Collectors.toList());

		try {
			writeToCsv(file, logEntries);
		} catch (IOException ioException) {
			LOGGER.error("Could not write to file {}, error: {}", file, ioException.getMessage());
		}
	}

	private void writeToCsv(final String filename, final List<String[]> dataLines) throws IOException {
		File csvOutputFile = new File(filename);
		csvOutputFile.createNewFile();

		try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
			dataLines.stream()
			.map(this::convertToCSV)
			.forEach(pw::println);
		}
	}

	private String convertToCSV(String[] data) {
		return Stream.of(data)
		.map(this::escapeSpecialCharacters)
		.collect(Collectors.joining(","));
	}


	private String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}
}
