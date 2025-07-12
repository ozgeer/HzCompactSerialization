package com.github.ozgeer.serializer;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import com.github.ozgeer.faculty.Faculty;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.utility.Entry;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class StudentSerializer implements CompactSerializer<Student> {
	@Override
	public Student read(CompactReader reader) {
		Student student = new Student();
		String name = reader.readString("name");
		int no = reader.readInt32("no");
		Long registerDate = reader.readNullableInt64("registerDate");
		if (registerDate != null) {
			student.setRegisterDate(Date.from(Instant.ofEpochSecond(registerDate)));
		}
		String department = reader.readString("department");
		Faculty faculty = reader.readCompact("faculty");

		//List<Lecture> lectureList = Arrays.asList(compactReader.readArrayOfCompact("listOfLecture", Lecture.class));

		HashMap<Integer, Lecture> lectures = new HashMap<>();
		Entry[] lectureArray = reader.readArrayOfCompact("lectures", Entry.class);

		for (Entry entry : lectureArray) {
			lectures.put((Integer) entry.getKey(), (Lecture) entry.getValue());
		}
		student.setName(name);
		student.setNo(no);
		student.setDepartment(department);
		student.setFaculty(faculty);
		student.setLectureHashMap(lectures);
		return student;
	}

	@Override
	public void write(CompactWriter writer, Student object) {
		writer.writeString("name", object.getName());
		writer.writeNullableInt32("no", object.getNo());
		writer.writeNullableInt64("registerDate", object.getRegisterDate() != null ? object.getRegisterDate().toInstant().getEpochSecond() : null);
		writer.writeString("faculty", String.valueOf(object.getFaculty()));
		writer.writeString("department", object.getDepartment());
		//compactWriter.writeArrayOfCompact("listOfLecture", student.listOfLecture().toArray());

		Entry[] entries = object.getLectureHashMap().entrySet().stream().map(entry -> new Entry<>(entry.getKey(), entry.getValue())).toArray(Entry[]::new);

		writer.writeArrayOfCompact("lectures", entries);
		writer.writeCompact("collegeName", object.getCollegeName());
	}

	@Override
	public String getTypeName() {
		return "Student";
	}

	@Override
	public Class<Student> getCompactClass() {
		return Student.class;
	}
}
