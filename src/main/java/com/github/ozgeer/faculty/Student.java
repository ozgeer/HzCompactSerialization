package com.github.ozgeer.faculty;

import java.util.HashMap;

import com.github.ozgeer.smf.model.SessionRule;

public class Student {
	private String name;
	private int no;
	private Faculty faculty;
	private String department;
	private HashMap<Integer, Lecture> lectureHashMap;
	private String field1;

	private String field4;

	private College collegeName;

	private SessionRule sessionRule;

	public Student(String name, int no, Faculty faculty, String department, HashMap<Integer, Lecture> lectureHashMap, String field1
			//	, String field2
			//	, int field3
			, String field4, College collegeName, SessionRule sessionRule) {
		this.name = name;
		this.no = no;
		this.faculty = faculty;
		this.department = department;
		this.lectureHashMap = lectureHashMap;
		this.field1 = field1;
		//	this.field2 = field2;
		//	this.field3 = field3;
		this.field4 = field4;
		this.collegeName = collegeName;
		this.sessionRule = sessionRule;
	}

	public Student() {
	}

	public String getName() {
		return name;
	}

	public int getNo() {
		return no;
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

	public String getField1() {
		return field1;
	}

	//	public String getField2() {
	//		return field2;
	//	}
	//
	//	public int getField3() {
	//		return field3;
	//	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setLectureHashMap(HashMap<Integer, Lecture> lectureHashMap) {
	}

/*	public QosParameters getQosParameters() {
		return qosParameters;
	}

	public void setQosParameters(QosParameters qosParameters) {
		this.qosParameters = qosParameters;
	}*/

	public SessionRule getSessionRule() {
		return sessionRule;
	}

	public void setSessionRule(SessionRule sessionRule) {
		this.sessionRule = sessionRule;
	}

	@Override
	public String toString() {
		return "Student{" + "name='" + name + '\'' + ", no=" + no + ", faculty=" + faculty + ", department='" + department + '\'' + ", lectureHashMap="
				+ lectureHashMap + ", field1='" + field1 + '\'' +
				//				", field2='" + field2 + '\'' +
				//				", field3=" + field3 +
				", field4=" + field4 + ", collegeName=" + collegeName + "sessionRule=" + sessionRule + '}';
	}
}


