package com.github.ozgeer.serializer;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

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
		college.setCollegeURI(reader.readCompact("collegeURI"));
		try {
			college.setIpAddress(InetAddress.getByName(reader.readString("ipAddress")));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		return college;
	}

	@Override
	public void write(CompactWriter writer, College object) {
		writer.writeString("name", object.getCollegeName());
		writer.writeString("cityName",object.getCollegeCity());
		writer.writeCompact("collegeURI",object.getCollegeURI());
		writer.writeString("ipAddress",object.getIpAddress().getHostName());

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
