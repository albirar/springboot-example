package com.example.demo.services.impl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.demo.models.LogbookEntryBean;
import com.example.demo.services.DemoService;
import com.example.demo.services.RangeSearchBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class DemoServiceImpl implements DemoService, InitializingBean {
	private LogbookBean logbook;
	@Value("classpath:/basic-data.json")
	private Resource basicData;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String content;
		ObjectMapper mapper;
		LogbookBean lb;

		mapper = new ObjectMapper();
		mapper.registerModules(new Jdk8Module(), new JavaTimeModule());
		/*
		 * Load basic data as logbook
		 */
		content = basicData.getContentAsString(StandardCharsets.UTF_8);
		lb = mapper.readValue(content, LogbookBean.class);
		logbook = LogbookBean.builder().build();
		logbook.getEntries().addAll(lb.getEntries());
	}

	@Override
	public @NonNull List<LogbookEntryBean> findLogEntries(@NonNull RangeSearchBean range) {
		Instant t1;
		Instant t2;
		
		t1 = range.getFrom().orElse(Instant.ofEpochMilli(0L));
		t2 = range.getTo().orElse(Instant.now());
		synchronized (logbook.getEntries()) {
			return logbook.getEntries().subSet(LogbookEntryBean.builder().timestamp(t1).build(), LogbookEntryBean.builder().timestamp(t2).build()).stream().toList();
		}
	}
	@Override
	public boolean addEntry(LogbookEntryBean entry) {
		synchronized (logbook.getEntries()) {
			return logbook.getEntries().add(entry.toBuilder().build());
		}
	}

	@Override
	public Optional<LogbookEntryBean> findByTimestamp(Instant ts) {
		LogbookEntryBean lb;
		
		lb = LogbookEntryBean.builder().timestamp(ts).build();
		synchronized (logbook.getEntries()) {
			if(logbook.getEntries().contains(lb)) {
				return Optional.of(logbook.getEntries().tailSet(LogbookEntryBean.builder().timestamp(ts).build()).first().toBuilder().build());
			}
		}
		return Optional.empty();
	}
}
