package com.example.demo.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.lang.NonNull;

import com.example.demo.models.LogbookEntryBean;

public interface DemoService {
	public @NonNull List<LogbookEntryBean> findLogEntries(@NonNull RangeSearchBean range);
	public @NonNull Optional<LogbookEntryBean> findByTimestamp(@NonNull Instant ts);
	public boolean addEntry(@NonNull LogbookEntryBean entry);
}
