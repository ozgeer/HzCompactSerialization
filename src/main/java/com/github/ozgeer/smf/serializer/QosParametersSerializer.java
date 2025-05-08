package com.github.ozgeer.smf.serializer;

import com.github.ozgeer.smf.model.QosParameters;
import com.hazelcast.nio.serialization.FieldKind;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class QosParametersSerializer implements CompactSerializer<QosParameters> {
	@Override
	public QosParameters read(CompactReader reader) {
		QosParameters qosParameters = new QosParameters();
		qosParameters.setId(reader.readString("id"));
		qosParameters.setBindingParameters(reader.readCompact("bindingParameters"));
		qosParameters.setReflective(reader.readNullableBoolean("reflective"));
		if (reader.getFieldKind("deneme") == FieldKind.STRING) {
			qosParameters.setDeneme(reader.readString("deneme"));
		}

		return qosParameters;
	}

	@Override
	public void write(CompactWriter writer, QosParameters object) {
		writer.writeString("id", object.getId());
		writer.writeCompact("bindingParameters", object.getBindingParameters());
		writer.writeNullableBoolean("reflective", object.isReflective());
		writer.writeString("deneme", object.getDeneme());

	}

	@Override
	public String getTypeName() {
		return "QosParameters";
	}

	@Override
	public Class<QosParameters> getCompactClass() {
		return QosParameters.class;
	}
}
