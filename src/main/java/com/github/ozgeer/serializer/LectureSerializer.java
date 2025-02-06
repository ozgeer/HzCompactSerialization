package com.github.ozgeer.serializer;

import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class LectureSerializer implements CompactSerializer<Lecture> {
	@Override
	public Lecture read(CompactReader compactReader) {
		String name = compactReader.readString("name");
		Instructor instructor = compactReader.readCompact("instructor"); // onemli.
		//	List<Student> studentList = Arrays.asList(compactReader.readArrayOfCompact("studentList", Student.class));
		String content = compactReader.readString("content");
		return new Lecture(name, instructor, content);
	}

	@Override
	public void write(CompactWriter compactWriter, Lecture lecture) {
		compactWriter.writeString("name", lecture.name());
		compactWriter.writeCompact("instructor", lecture.instructor());
		//	compactWriter.writeArrayOfCompact("studentList",lecture.listOfStudent().toArray());
		compactWriter.writeString("content", lecture.content());

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
