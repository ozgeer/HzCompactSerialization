package com.github.ozgeer.serializer;

import com.github.ozgeer.faculty.College;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class CollegeSerializer implements CompactSerializer<College> {
	@Override
	public College read(CompactReader reader) {
		College college = new College();
		college.setCollegeCity(reader.readString("name"));
		college.setCollegeCity(reader.readString("cityName"));
		return college;
	}

	@Override
	public void write(CompactWriter writer, College object) {
		writer.writeString("name", object.getCollegeName());
		writer.writeString("cityName",object.getCollegeCity());

	}

	@Override
	public String getTypeName() {
		return "college";
	}

	@Override
	public Class<College> getCompactClass() {
		return College.class;
	}
}
