package com.github.ozgeer.faculty;

import java.util.UUID;

public class Instructor {

	String name;

	String surname;

	int no;

	UUID uuid;

	College college;

	public Instructor() {
	}

	public Instructor(String name, String surname, College college
	) {
		this.name = name;
		this.surname = surname;
		this.college = college;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public College getCollege() {
		return college;
	}

	public static Instructor of(String name, String surname, College college
	) {
		return new Instructor(name, surname,college
		);
	}

	@Override
	public String toString() {
		StringBuilder instructor = new StringBuilder();
		instructor.append("[name:").append(getName()).append(" surname:").append(getSurname()).append(" no:").append(getNo()).append(" college:").append(getCollege()).append("]");
		return instructor.toString();
	}

}
