package com.github.ozgeer.Serializer;

import java.util.Arrays;
import java.util.List;

import com.github.ozgeer.Faculty.Instructor;
import com.github.ozgeer.Faculty.Lecture;
import com.github.ozgeer.Faculty.Student;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class LectureSerializer implements CompactSerializer<Lecture> {
	@Override
	public Lecture read(CompactReader compactReader) {
		String name = compactReader.readString("name");
		Instructor instructor = compactReader.readCompact("instructor"); // onemli.
		List<Student> studentList = Arrays.asList(compactReader.readArrayOfCompact("studentList", Student.class));
		String content = compactReader.readString("content");
		return new Lecture(name,instructor,studentList,content);
	}

	@Override
	public void write(CompactWriter compactWriter, Lecture lecture) {
		compactWriter.writeString("name",lecture.name());
		compactWriter.writeCompact("instructor",lecture.instructor());
		compactWriter.writeArrayOfCompact("studentList",lecture.listOfStudent().toArray());
		compactWriter.writeString("content",lecture.content());

	}

	@Override
	public String getTypeName() {
		return "lecture";
	}

	@Override
	public Class<Lecture> getCompactClass() {
		return Lecture.class;
	}
}
