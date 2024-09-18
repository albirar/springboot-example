package com.example.demo.services.impl;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.example.demo.models.LogbookEntryBean;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonInclude(value = NON_EMPTY)
public class LogbookBean {
	@Default
	private SortedSet<LogbookEntryBean> entries = Collections.synchronizedSortedSet(new TreeSet<>());
}
