package com.github.ozgeer.faculty;

import java.util.List;


public record Lecture(String name, Instructor instructor, String content) {

	public Lecture(String name, Instructor instructor) {
		this(name, instructor,null);
	}

	public static Lecture of(String name, Instructor instructor) {
		return new Lecture(name, instructor,null);
	}

}
