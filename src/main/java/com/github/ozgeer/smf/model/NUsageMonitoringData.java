package com.github.ozgeer.smf.model;

import java.util.Date;
import java.util.List;

public class NUsageMonitoringData {
	private static final long serialVersionUID = 1L;
	private String umId;
	private Long volumeThreshold;
	private Long volumeThresholdUplink;
	private Long volumeThresholdDownlink;
	private Integer timeThreshold;
	private Date monitoringTime;
	private Long nextVolThreshold;
	private Long nextVolThresholdUplink;
	private Long nextVolThresholdDownlink;
	private Integer nextTimeThreshold;
	private Integer inactivityTime;
	private List<String> exUsagePccRuleIds;

	public NUsageMonitoringData() {

	}

	public String getUmId() {
		return umId;
	}

	public void setUmId(String umId) {
		this.umId = umId;
	}

	public Long getVolumeThreshold() {
		return volumeThreshold;
	}

	public void setVolumeThreshold(Long volumeThreshold) {
		this.volumeThreshold = volumeThreshold;
	}

	public Long getVolumeThresholdUplink() {
		return volumeThresholdUplink;
	}

	public void setVolumeThresholdUplink(Long volumeThresholdUplink) {
		this.volumeThresholdUplink = volumeThresholdUplink;
	}

	public Long getVolumeThresholdDownlink() {
		return volumeThresholdDownlink;
	}

	public void setVolumeThresholdDownlink(Long volumeThresholdDownlink) {
		this.volumeThresholdDownlink = volumeThresholdDownlink;
	}

	public Integer getTimeThreshold() {
		return timeThreshold;
	}

	public void setTimeThreshold(Integer timeThreshold) {
		this.timeThreshold = timeThreshold;
	}

	public Date getMonitoringTime() {
		return monitoringTime;
	}

	public void setMonitoringTime(Date monitoringTime) {
		this.monitoringTime = monitoringTime;
	}

	public Long getNextVolThreshold() {
		return nextVolThreshold;
	}

	public void setNextVolThreshold(Long nextVolThreshold) {
		this.nextVolThreshold = nextVolThreshold;
	}

	public Long getNextVolThresholdUplink() {
		return nextVolThresholdUplink;
	}

	public void setNextVolThresholdUplink(Long nextVolThresholdUplink) {
		this.nextVolThresholdUplink = nextVolThresholdUplink;
	}

	public Long getNextVolThresholdDownlink() {
		return nextVolThresholdDownlink;
	}

	public void setNextVolThresholdDownlink(Long nextVolThresholdDownlink) {
		this.nextVolThresholdDownlink = nextVolThresholdDownlink;
	}

	public Integer getNextTimeThreshold() {
		return nextTimeThreshold;
	}

	public void setNextTimeThreshold(Integer nextTimeThreshold) {
		this.nextTimeThreshold = nextTimeThreshold;
	}

	public Integer getInactivityTime() {
		return inactivityTime;
	}

	public void setInactivityTime(Integer inactivityTime) {
		this.inactivityTime = inactivityTime;
	}

	public List<String> getExUsagePccRuleIds() {
		return exUsagePccRuleIds;
	}

	public void setExUsagePccRuleIds(List<String> exUsagePccRuleIds) {
		this.exUsagePccRuleIds = exUsagePccRuleIds;
	}

}
