package com.maddob.time.clockifywizard.models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record ClockifyLogEntry(
		String project, 
		String client, 
		String description, 
		String task, 
		String user, 
		String group, 
		String email, 
		String tags, 
		String billable, 
		LocalDate startDate, 
		LocalTime startTime, 
		LocalDate endDate, 
		LocalTime endTime, 
		Duration duration, 
		double durationDecimal, 
		String billableRate, 
		String billableAmount){
	
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");


	public ClockifyLogEntry(String[] parameters) {
		this(parameters[0],
				parameters[1],
				parameters[2],
				parameters[3],
				parameters[4],
				parameters[5],
				parameters[6],
				parameters[7],
				parameters[8],
				LocalDate.parse(parameters[9], dateFormatter),
				LocalTime.parse(parameters[10], timeFormatter),
				LocalDate.parse(parameters[11], dateFormatter),
				LocalTime.parse(parameters[12], timeFormatter),
				Duration.between (                  // Represent a span of time of hours, minutes, seconds. 
				    LocalTime.MIN ,                 // 00:00:00
				    LocalTime.parse ( parameters[13] )  // Parse text as a time-of-day. 
				),
				Double.valueOf(parameters[14]),
				parameters[15],
				parameters[16]
				);
	}



	public String getJiraTicketNumber() {
		Pattern pattern = Pattern.compile("FISCO-\\d{3,4}+");
		Matcher matcher = pattern.matcher(this.task.toUpperCase());

        List<String> listMatches = new ArrayList<String>();

		// check task field for matches
        while(matcher.find()) {
            listMatches.add(matcher.group());
        }
		if (!listMatches.isEmpty()) {
			return listMatches.get(0);
		}

		// no task field matches, check description
		matcher = pattern.matcher(this.description.toUpperCase());
        while(matcher.find()) {
            listMatches.add(matcher.group());
        }
		if (!listMatches.isEmpty()) {
			return listMatches.get(0);
		}

		return "";
	}
}
