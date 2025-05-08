package com.github.ozgeer.faculty;

import com.hazelcast.shaded.org.checkerframework.checker.nullness.qual.Nullable;

public class Lecture {
	private String name;
	private Instructor instructor;
	private String content;

	private Integer id;

	public Lecture() {
		// Boş constructor lazım. Cunku zero-config ile calısacagız bu classı.
	}

	public Lecture(String name, Instructor instructor, String content
		//	, @Nullable Integer id
	) {
		this.name = name;
		this.instructor = instructor;
		this.content = content;
	}

	public Lecture(String name, Instructor instructor) {
		this(name, instructor,null);
	}

	public static Lecture of(String name, Instructor instructor) {
		return new Lecture(name, instructor);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	//	@Nullable
	//	public Integer getId() {
	//		return id;
	//	}
	//
	//	public void setId( Integer id) {
	//		this.id = id;
	//	}

	@Override
	public String toString() {
		return "Lecture{" + "name='" + name + '\'' + ", instructor=" + instructor + ", content='" + content + '\'' +
				", id=" + id +
				'}';
	}
}
