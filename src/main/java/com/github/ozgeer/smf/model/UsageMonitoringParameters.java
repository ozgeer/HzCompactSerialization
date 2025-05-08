package com.github.ozgeer.smf.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsageMonitoringParameters {
	private static final long serialVersionUID = 3582116084426774703L;

	@JsonProperty("usageMonitoringData")
	private NUsageMonitoringData usageMonitoringData;
	@JsonProperty("requestTriggers")
	private List<NPolicyControlRequestTrigger> requestTriggers;

	public UsageMonitoringParameters(NUsageMonitoringData usageMonitoringData, List<NPolicyControlRequestTrigger> requestTriggers) {
		this.usageMonitoringData = usageMonitoringData;
		this.requestTriggers = requestTriggers;
	}


	public NUsageMonitoringData getUsageMonitoringData() {
		return usageMonitoringData;
	}

	public void setUsageMonitoringData(NUsageMonitoringData usageMonitoringData) {
		this.usageMonitoringData = usageMonitoringData;
	}

	public List<NPolicyControlRequestTrigger> getRequestTriggers() {
		return requestTriggers;
	}

	public void setRequestTriggers(List<NPolicyControlRequestTrigger> requestTriggers) {
		this.requestTriggers = requestTriggers;
	}
}
