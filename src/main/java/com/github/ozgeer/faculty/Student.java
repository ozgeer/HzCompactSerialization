package com.github.ozgeer.faculty;

import java.util.Date;
import java.util.HashMap;

public class Student {
	private String name;
	private Integer studentNo;
	private Date registerDate;
	private Faculty faculty;
	private String department;
	private HashMap<Integer, Lecture> lectureHashMap;

	private College collegeName;

	public Student(String name, Integer no, Date registerDate, Faculty faculty, String department, HashMap<Integer, Lecture> lectureHashMap,
			College collegeName) {
		this.name = name;
		this.studentNo = no;
		this.registerDate = registerDate;
		this.faculty = faculty;
		this.department = department;
		this.lectureHashMap = lectureHashMap;
		this.collegeName = collegeName;
	}

	public Student() {
	}

	public String getName() {
		return name;
	}

	public Integer getNo() {
		return studentNo;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public String getDepartment() {
		return department;
	}

	public HashMap<Integer, Lecture> getLectureHashMap() {
		return lectureHashMap;
	}

	public College getCollegeName() {
		return collegeName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNo(int no) {
		this.studentNo = no;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setLectureHashMap(HashMap<Integer, Lecture> lectureHashMap) {
	}

	@Override
	public String toString() {
		return "Student{" + "name='" + name + '\'' + ", no=" + studentNo + ", faculty=" + faculty + ", department='" + department + '\'' + ", lectureHashMap="
				+ lectureHashMap + ", collegeName=" + collegeName + '}';
	}
}


