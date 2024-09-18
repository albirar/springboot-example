package com.example.demo.services;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.time.Instant;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder.Default;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Value
@SuperBuilder(toBuilder = true)
@Jacksonized
@JsonInclude(value = NON_EMPTY)
public class RangeSearchBean {
	@Default
	private Optional<Instant> from = Optional.empty();
	@Default
	private Optional<Instant> to = Optional.empty();
}
