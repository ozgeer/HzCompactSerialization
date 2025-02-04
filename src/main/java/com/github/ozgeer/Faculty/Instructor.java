package com.github.ozgeer.faculty;

import java.util.UUID;

public class Instructor {

	String name;

	int no;

	public Instructor(String name, int no) {
		this.name = name;
		this.no = no;
		 // this.uuid=uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

//	public UUID getUuid() {
//		return uuid;
//	}
//
//	public void setUuid(UUID uuid) {
//		this.uuid = uuid;
//	}

	public static Instructor of(String name, int no) {
		return new Instructor(name, no);
	}

	@Override
	public String toString() {
		StringBuilder instructor = new StringBuilder();
		instructor.append("name: ").append(getName());
		return instructor.toString();
	}

}
