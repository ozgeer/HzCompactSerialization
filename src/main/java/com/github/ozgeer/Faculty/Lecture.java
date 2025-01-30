package com.github.ozgeer.Faculty;

import java.util.List;

public record Lecture(String name, Instructor instructor, List<Student> listOfStudent, String content) {

	public Lecture(String name, Instructor instructor, List<Student> listOfStudent) {
		this(name, instructor, listOfStudent, null);
	}

	public static Lecture of(String name, Instructor instructor, List<Student> listOfStudent) {
		return new Lecture(name, instructor, listOfStudent);
	}

}
