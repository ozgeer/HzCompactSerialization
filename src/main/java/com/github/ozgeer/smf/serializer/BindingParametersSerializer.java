package com.github.ozgeer.smf.serializer;

import com.github.ozgeer.smf.model.BindingParameters;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class BindingParametersSerializer implements CompactSerializer<BindingParameters> {
	@Override
	public BindingParameters read(CompactReader reader) {
		BindingParameters bindingParameters = new BindingParameters();
		bindingParameters.setBindToDefault(reader.readBoolean("bindToDefault"));
		bindingParameters.setN5qi(reader.readInt32("n5qi"));
		bindingParameters.setQnc(reader.readBoolean("qnc"));
		bindingParameters.setResourcePriority(reader.readInt32("resourcePriority"));
		bindingParameters.setAveragingWindow(reader.readInt32("averagingWindow"));
		bindingParameters.setMaxDataBurstVolume(reader.readInt32("maxDataBurstVolume"));
		bindingParameters.setBindingKey(reader.readInt32("bindingKey"));
		return bindingParameters;
	}

	@Override
	public void write(CompactWriter writer, BindingParameters object) {
		writer.writeNullableBoolean("bindToDefault",object.isBindToDefault());
		writer.writeNullableInt32("n5qi",object.getN5qi());
		writer.writeNullableBoolean("qnc",object.getQnc());
		writer.writeNullableInt32("resourcePriority", object.getResourcePriority());
		writer.writeInt32("averagingWindow",object.getAveragingWindow());
		writer.writeInt32("maxDataBurstVolume",object.getMaxDataBurstVolume());
		writer.writeInt32("bindingKey",object.getBindingKey());
	}

	@Override
	public String getTypeName() {
		return "BindingParameters";
	}

	@Override
	public Class<BindingParameters> getCompactClass() {
		return BindingParameters.class;
	}
}
