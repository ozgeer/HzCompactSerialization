package com.github.ozgeer.smf.model;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionRule {
	@Serial
	private static final long serialVersionUID = -4888906237549695065L;
	@JsonProperty("sessionRuleId")
	private String sessionRuleId;
	@JsonProperty("qosParameters")
	private QosParameters qosParameters;
	@JsonProperty("usageMonitoringParameters")
	private UsageMonitoringParameters usageMonitoringParameters;
	@JsonProperty("conditionData")
	private ConditionData conditionData;

	public SessionRule() {

	}

	public SessionRule(String sessionRuleId, QosParameters qosParameters, UsageMonitoringParameters usageMonitoringParameters, ConditionData conditionData) {
		this.sessionRuleId = sessionRuleId;
		this.qosParameters = qosParameters;
		this.usageMonitoringParameters = usageMonitoringParameters;
		this.conditionData = conditionData;
	}

	@Override
	public String toString() {
		return "{" +
				"sessionRuleId='" + sessionRuleId + '\'' +
				", qosParameters=" + qosParameters +
				", usageMonitoringParameters=" + usageMonitoringParameters +
				", conditionData=" + conditionData +
				'}';
	}


	public String getSessionRuleId() {
		return sessionRuleId;
	}

	public void setSessionRuleId(String sessionRuleId) {
		this.sessionRuleId = sessionRuleId;
	}

	public QosParameters getQosParameters() {
		return qosParameters;
	}

	public void setQosParameters(QosParameters qosParameters) {
		this.qosParameters = qosParameters;
	}

	public UsageMonitoringParameters getUsageMonitoringParameters() {
		return usageMonitoringParameters;
	}

	public void setUsageMonitoringParameters(UsageMonitoringParameters usageMonitoringParameters) {
		this.usageMonitoringParameters = usageMonitoringParameters;
	}

	public ConditionData getConditionData() {
		return conditionData;
	}

	public void setConditionData(ConditionData conditionData) {
		this.conditionData = conditionData;
	}

}
