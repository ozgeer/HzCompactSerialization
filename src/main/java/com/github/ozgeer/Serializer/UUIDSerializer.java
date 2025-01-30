//package org.example.Serializer;
//
//import java.util.Objects;
//import java.util.UUID;
//
//import com.hazelcast.nio.serialization.compact.CompactReader;
//import com.hazelcast.nio.serialization.compact.CompactSerializer;
//import com.hazelcast.nio.serialization.compact.CompactWriter;
//
//public class UUIDSerializer implements CompactSerializer<UUID> {
//	@Override
//	public UUID read(CompactReader reader) {
//		return UUID.fromString(Objects.requireNonNull(reader.readString("uuid")));
//	}
//
//	@Override
//	public void write(CompactWriter writer, UUID object) {
//		writer.writeString("uuid",object.toString());
//	}
//
//	@Override public String getTypeName() {
//		return "UUID";
//	}
//
//	@Override
//	public Class<UUID> getCompactClass() {
//		return UUID.class;
//	}
//}
