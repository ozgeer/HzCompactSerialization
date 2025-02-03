package com.github.ozgeer.serializer;

import java.util.HashMap;

import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;

import com.github.ozgeer.utility.Entry;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class StudentSeriliazer implements CompactSerializer<Student> {
	@Override
	public Student read(CompactReader compactReader) {
		String name = compactReader.readString("name");
		int no = compactReader.readInt32("no");
		String department = compactReader.readString("department");

		//List<Lecture> lectureList = Arrays.asList(compactReader.readArrayOfCompact("listOfLecture", Lecture.class));

		HashMap<Integer, Lecture> lectures = new HashMap<>();
		Entry[] lectureArray = compactReader.readArrayOfCompact("lectures", Entry.class);

		for (Entry entry : lectureArray) {
			lectures.put((Integer) entry.getKey(), (Lecture) entry.getValue());
		}
		return new Student(name, no, department, lectures);
	}

//		HashMap<Lecture, Instructor> match= new HashMap<>();
//		Entry [] matchOfArray = compactReader.readArrayOfCompact("matchOfMap",Entry.class);
//
//		for(Entry entry: matchOfArray){
//			match.put((Lecture) entry.getKey(), (Instructor) entry.getValue());
//		}
//		return new Student(name, no, department, lectures,matchOfArray);


	@Override
	public void write(CompactWriter compactWriter, Student student) {
		compactWriter.writeString("name", student.name());
		compactWriter.writeInt32("no", student.no());
		compactWriter.writeString("department", student.department());
		//compactWriter.writeArrayOfCompact("listOfLecture", student.listOfLecture().toArray());

		Entry[] entries = student.lectureHashMap().entrySet().stream()
				.map(entry -> new Entry<>(entry.getKey(), entry.getValue()))
				.toArray(Entry[]::new);

		compactWriter.writeArrayOfCompact("lectures", entries);

//		Entry[] matches = student.matchOfMap().entrySet().stream() //
//				.map(entry -> new Entry<>(entry.getKey(),entry.getValue())) //
//				.toArray(Entry[]::new);
//
//		compactWriter.writeArrayOfCompact("matchOfMap",matches);  //  I'll keep going to development for HashMap<ComplexType,ComplexType>
	}

	@Override
	public String getTypeName() {
		return "student";
	}

	@Override
	public Class<Student> getCompactClass() {
		return Student.class;
	}
}
