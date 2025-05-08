package com.github.ozgeer.smf.model;

public enum NPreemptionCapability {
	NOT_PREEMPT("NOT_PREEMPT"),

	MAY_PREEMPT("MAY_PREEMPT");

	private String value;

	NPreemptionCapability(String value) {
		this.value = value;
	}
}
