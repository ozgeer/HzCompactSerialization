package com.github.ozgeer.serializer;
import com.github.ozgeer.faculty.Instructor;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class InstructorSerializer implements CompactSerializer<Instructor> {
	@Override
	public Instructor read(CompactReader compactReader) {
		Instructor instructor = new Instructor();
		instructor.setName(compactReader.readString("name"));
		instructor.setSurname(compactReader.readString("surname"));
//		instructor.setNo(compactReader.readNullableInt32("no"));
		return instructor;
	}

	@Override
	public void write(CompactWriter compactWriter, Instructor instructor) {
		compactWriter.writeString("name", instructor.getName());
		compactWriter.writeString("surname", instructor.getSurname());
//		compactWriter.writeNullableInt32("no", instructor.getNo());
		//compactWriter.writeCompact("uuid",instructor.getUuid());
	}

	@Override
	public String getTypeName() {
		return "instructor";
	}

	@Override
	public Class<Instructor> getCompactClass() {
		return Instructor.class;
	}
}
