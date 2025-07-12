package com.github.ozgeer.serializer;

import com.github.ozgeer.utility.Entry;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class EntrySerializer implements CompactSerializer<Entry<?, ?>> {

	@Override
	public Entry<?, ?> read(CompactReader reader) {
		// read type
		String keyType = reader.readString("keyType");
		String valueType = reader.readString("valueType");

		// read key dynamically
		Object key = readDynamicValue(reader, "key", keyType);

		// read value dynamically
		Object value = readDynamicValue(reader, "value", valueType);

		// create entry instance
		return new Entry<>(key, value);
	}

	@Override
	public void write(CompactWriter writer, Entry<?, ?> entry) {
		// determine key type and write
		String keyType = entry.getKey().getClass().getName();
		writer.writeString("keyType", keyType);
		writeDynamicValue(writer, "key", entry.getKey(), keyType);

		// determine value type and write
		String valueType = entry.getValue().getClass().getName();
		writer.writeString("valueType", valueType);
		writeDynamicValue(writer, "value", entry.getValue(), valueType);
	}

	@Override
	public String getTypeName() {
		return "Entry";
	}

	@Override
	public Class<Entry<?, ?>> getCompactClass() {
		return (Class<Entry<?, ?>>) (Class<?>) Entry.class;
	}

	// dynamic value read
	private Object readDynamicValue(CompactReader reader, String fieldName, String type) {
		switch (type) {
		//it may diversify
		case "java.lang.Integer":
			return reader.readInt32(fieldName);
		case "java.lang.String":
			return reader.readString(fieldName);
		default:
			return reader.readCompact(fieldName); // Complex type
		}
	}

	// dynamic value write
	private void writeDynamicValue(CompactWriter writer, String fieldName, Object value, String type) {
		switch (type) {
		// it may diversify
		case "java.lang.Integer":
			writer.writeInt32(fieldName, (Integer) value);
			break;
		case "java.lang.String":
			writer.writeString(fieldName, (String) value);
			break;
		default:
			writer.writeCompact(fieldName, value); // Complex type
			break;
		}
	}
}