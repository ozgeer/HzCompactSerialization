package com.github.ozgeer.faculty;

import java.net.InetAddress;
import java.net.URI;

public class College {
	String collegeName;

	String collegeCity;

	URI collegeURI;

	private InetAddress ipAddress;

	public College() {

	}

	public College(String name, URI uri, InetAddress ipAddress) {
		this.collegeName = name;
		this.collegeURI = uri;
		this.ipAddress = ipAddress;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public String getCollegeCity() {
		return collegeCity;
	}

	public void setCollegeCity(String collegeCity) {
		this.collegeCity = collegeCity;
	}

	public URI getCollegeURI() {
		return collegeURI;
	}

	public void setCollegeURI(URI collegeURI) {
		this.collegeURI = collegeURI;
	}


	public InetAddress  getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}



	@Override
	public String toString() {
		return "College{" +
				"collegeName='" + collegeName + '\'' + ", collegeCity='" + collegeCity + '\'' + ", collegeURI='" + collegeURI + '\'' + ", collegeIP='"
				+ getIpAddress() + '\'' +
				'}';
	}
}
