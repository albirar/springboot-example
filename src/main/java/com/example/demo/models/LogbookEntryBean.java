package com.example.demo.models;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonInclude(value = NON_EMPTY)
public class LogbookEntryBean implements Comparable<LogbookEntryBean>{
	private Instant timestamp;
	private String author;
	private String entry;
	@Override
	public int compareTo(LogbookEntryBean o) {
		return timestamp.compareTo(o.getTimestamp());
	}
}
