package com.github.ozgeer.smf.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConditionData {
	private static final long serialVersionUID = 1L;
	@JsonProperty("id")
	private final String id;
	@JsonProperty("activationTime")
	private final Instant activationTime;
	@JsonProperty("deactivationTime")
	private final Instant deactivationTime;

	private ConditionData(String id, Instant activationTime, Instant deactivationTime) {
		this.id = id;
		this.activationTime = activationTime;
		this.deactivationTime = deactivationTime;
	}
}
