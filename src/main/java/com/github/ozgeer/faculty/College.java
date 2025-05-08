package com.github.ozgeer.faculty;

public class College {

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getCollegeCity() {
		return collegeCity;
	}

	public void setCollegeCity(String collegeCity) {
		this.collegeCity = collegeCity;
	}

	String collegeName;

	String collegeCity;

	public College() {

	}

	public College(String name) {
		this.collegeName = name;
	}

	@Override
	public String toString() {
		return "College{" +
				"collegeName='" + collegeName + '\'' +
				", collegeCity='" + collegeCity + '\'' +
				'}';
	}
}
