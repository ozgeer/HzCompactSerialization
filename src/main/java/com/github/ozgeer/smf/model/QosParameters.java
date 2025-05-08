package com.github.ozgeer.smf.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QosParameters {
	private static final long serialVersionUID = -2231595718115320544L;
	@JsonProperty("id")
	private String id;
	@JsonProperty("bindingParameters")
	private BindingParameters bindingParameters;
	@JsonProperty("mbrUl")
	private BitRate mbrUl;
	@JsonProperty("mbrDl")
	private BitRate mbrDl;
	@JsonProperty("gbrUl")
	private BitRate gbrUl;
	@JsonProperty("gbrDl")
	private BitRate gbrDl;
	@JsonProperty("isReflective")
	private boolean reflective;

	private String deneme;

	public QosParameters() {

	}

	public QosParameters(String id, BindingParameters bindingParameters, BitRate mbrUl, BitRate mbrDl, BitRate gbrUl, BitRate gbrDl, boolean isReflective,
			String deneme) {
		this.id = id;
		this.bindingParameters = bindingParameters;
		this.mbrUl = mbrUl;
		this.mbrDl = mbrDl;
		this.gbrUl = gbrUl;
		this.gbrDl = gbrDl;
		this.reflective = isReflective;
		this.deneme = deneme;
	}

	@Override
	public String toString() {
		return "{" + "id='" + id + '\'' + ", bindingParameters=" + bindingParameters + ", mbrUl=" + mbrUl + ", mbrDl=" + mbrDl + ", gbrUl=" + gbrUl + ", gbrDl="
				+ gbrDl + ", reflective=" + reflective + ", deneme=" + deneme + '}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BindingParameters getBindingParameters() {
		return bindingParameters;
	}

	public void setBindingParameters(BindingParameters bindingParameters) {
		this.bindingParameters = bindingParameters;
	}

	public BitRate getMbrUl() {
		return mbrUl;
	}

	public void setMbrUl(BitRate mbrUl) {
		this.mbrUl = mbrUl;
	}

	public BitRate getMbrDl() {
		return mbrDl;
	}

	public void setMbrDl(BitRate mbrDl) {
		this.mbrDl = mbrDl;
	}

	public BitRate getGbrUl() {
		return gbrUl;
	}

	public void setGbrUl(BitRate gbrUl) {
		this.gbrUl = gbrUl;
	}

	public BitRate getGbrDl() {
		return gbrDl;
	}

	public void setGbrDl(BitRate gbrDl) {
		this.gbrDl = gbrDl;
	}

	public boolean isReflective() {
		return reflective;
	}

	public void setReflective(boolean reflective) {
		this.reflective = reflective;
	}

	public String getDeneme() {
		return deneme;
	}

	public void setDeneme(String deneme) {
		this.deneme = deneme;
	}

}
