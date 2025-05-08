package com.github.ozgeer.smf.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BindingParameters {

	@JsonProperty("bindToDefault")
	private boolean bindToDefault;
	@JsonProperty("n5qi")
	private Integer n5qi;
	@JsonProperty("arp")
	private NArp arp;
	@JsonProperty("qnc")
	private Boolean qnc;
	@JsonProperty("resourcePriority")
	private Integer resourcePriority;
	@JsonProperty("averagingWindow")
	private Integer averagingWindow;
	@JsonProperty("maxDataBurstVolume")
	private Integer maxDataBurstVolume;
	@JsonProperty("bindingKey")
	private int bindingKey;

	public BindingParameters(){

	}

	public BindingParameters(boolean isBindToDefault, Integer n5qi, NArp arp, Boolean qnc, Integer resourcePriority, Integer averagingWindow,
			Integer maxDataBurstVolume) {
		this.bindToDefault = isBindToDefault;
		this.n5qi = n5qi;
		this.arp = arp;
		this.qnc = qnc;
		this.resourcePriority = resourcePriority;
		this.averagingWindow = averagingWindow;
		this.maxDataBurstVolume = maxDataBurstVolume;

		bindingKey = isBindToDefault ? Objects.hashCode(true) : Objects.hash(n5qi, arp, qnc, resourcePriority, averagingWindow, maxDataBurstVolume);
	}

	public String toString() {
		return "{" +
				"bindToDefault=" + bindToDefault +
				", n5qi=" + n5qi +
				", arp=" + arp +
				", qnc=" + qnc +
				", resourcePriority=" + resourcePriority +
				", averagingWindow=" + averagingWindow +
				", maxDataBurstVolume=" + maxDataBurstVolume +
				", bindingKey=" + bindingKey +
				'}';
	}

	public boolean isBindToDefault() {
		return bindToDefault;
	}

	public void setBindToDefault(boolean bindToDefault) {
		this.bindToDefault = bindToDefault;
	}

	public Integer getN5qi() {
		return n5qi;
	}

	public void setN5qi(Integer n5qi) {
		this.n5qi = n5qi;
	}

	public NArp getArp() {
		return arp;
	}

	public void setArp(NArp arp) {
		this.arp = arp;
	}

	public Boolean getQnc() {
		return qnc;
	}

	public void setQnc(Boolean qnc) {
		this.qnc = qnc;
	}

	public Integer getResourcePriority() {
		return resourcePriority;
	}

	public void setResourcePriority(Integer resourcePriority) {
		this.resourcePriority = resourcePriority;
	}

	public Integer getAveragingWindow() {
		return averagingWindow;
	}

	public void setAveragingWindow(Integer averagingWindow) {
		this.averagingWindow = averagingWindow;
	}

	public Integer getMaxDataBurstVolume() {
		return maxDataBurstVolume;
	}

	public void setMaxDataBurstVolume(Integer maxDataBurstVolume) {
		this.maxDataBurstVolume = maxDataBurstVolume;
	}

	public int getBindingKey() {
		return bindingKey;
	}

	public void setBindingKey(int bindingKey) {
		this.bindingKey = bindingKey;
	}


}
