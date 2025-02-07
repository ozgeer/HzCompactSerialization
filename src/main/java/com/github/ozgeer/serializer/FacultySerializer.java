package com.github.ozgeer.serializer;

import com.github.ozgeer.faculty.Faculty;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class FacultySerializer implements CompactSerializer<Faculty> {
	@Override
	public Faculty read(CompactReader reader) {
		String faculty = reader.readString("faculty");
		return Faculty.valueOf(faculty);
	}

	@Override
	public void write(CompactWriter writer, Faculty object) {
		writer.writeString("faculty", object.name());
	}

	@Override
	public String getTypeName() {
		return Faculty.class.getName();
	}

	@Override
	public Class<Faculty> getCompactClass() {
		return Faculty.class;
	}
}
