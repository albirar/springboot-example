package com.example.demo.controllers;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.models.LogbookEntryBean;
import com.example.demo.services.DemoService;
import com.example.demo.services.RangeSearchBean;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/logbook")
public class DemoLogbookController {
	@Autowired
	private DemoService service;
	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<LogbookEntryBean>> findEntries(@RequestParam(required = false, name = "from") Instant from, @RequestParam(required = false, name = "to") Instant to) {
		RangeSearchBean range;
		
		range = RangeSearchBean.builder()
				.from(Optional.ofNullable(from))
				.to(Optional.ofNullable(to))
				.build();
		return ResponseEntity.ok(service.findLogEntries(range));
	}

	
	@GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, path = "/{ts}")
	public ResponseEntity<LogbookEntryBean> findEntry(@PathVariable(required = true, name = "ts") Instant entryInstant) {
		Optional<LogbookEntryBean> entry;
		
		entry = service.findByTimestamp(entryInstant);
		if(entry.isPresent()) {
			return ResponseEntity.ok(entry.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Void> postEntry(HttpServletRequest request, @RequestBody(required = true) LogbookEntryBean entry) {
		if(service.addEntry(entry)) {
			return ResponseEntity.created(createUriForEntry(entry)).build();
		}
		return ResponseEntity.status(HttpStatus.SEE_OTHER).location(createUriForEntry(entry)).build();
	}
	
	private URI createUriForEntry(LogbookEntryBean entry) {
		return ServletUriComponentsBuilder.fromCurrentRequest()
			.replacePath(entry.getTimestamp().toString())
			.build().toUri();
	}
}
