package com.github.ozgeer.Serializer;

import java.util.UUID;

import com.github.ozgeer.Faculty.Instructor;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class InstructorSerialize implements CompactSerializer<Instructor> {
	@Override
	public Instructor read(CompactReader compactReader) {
		String name = compactReader.readString("name");
		Integer no = compactReader.readInt32("no");
		UUID uuid =compactReader.readCompact("uuid");
		return new Instructor(name, no, uuid);
	}

	@Override
	public void write(CompactWriter compactWriter, Instructor instructor) {
		compactWriter.writeString("name", instructor.getName());
		compactWriter.writeInt32("no", instructor.getNo());
		compactWriter.writeCompact("uuid",instructor.getUuid());
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
