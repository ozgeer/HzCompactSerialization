package com.github.ozgeer.smf.model;

public class NArp {
	private static final long serialVersionUID = 1L;

	public Integer getPriorityLevel() {
		return priorityLevel;
	}

	public void setPriorityLevel(Integer priorityLevel) {
		this.priorityLevel = priorityLevel;
	}

	public NPreemptionCapability getPreemptCap() {
		return preemptCap;
	}

	public void setPreemptCap(NPreemptionCapability preemptCap) {
		this.preemptCap = preemptCap;
	}

	public NPreemptionVulnerability getPreemptVuln() {
		return preemptVuln;
	}

	public void setPreemptVuln(NPreemptionVulnerability preemptVuln) {
		this.preemptVuln = preemptVuln;
	}

	private Integer priorityLevel;
	private  NPreemptionCapability preemptCap;
	private  NPreemptionVulnerability preemptVuln;

	public NArp() {
	}

	public NArp (Integer priorityLevel, NPreemptionCapability preemptCap,
			NPreemptionVulnerability preemptVuln) {
		super();
		this.priorityLevel = priorityLevel;
		this.preemptCap = preemptCap;
		this.preemptVuln = preemptVuln;
	}
}
