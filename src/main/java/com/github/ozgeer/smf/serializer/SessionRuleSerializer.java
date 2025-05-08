package com.github.ozgeer.smf.serializer;

import com.github.ozgeer.smf.model.SessionRule;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class SessionRuleSerializer implements CompactSerializer<SessionRule> {
	@Override
	public SessionRule read(CompactReader reader) {
		SessionRule sessionRule= new SessionRule();
		sessionRule.setSessionRuleId(reader.readString("sessionRuleId"));
		sessionRule.setQosParameters(reader.readCompact("qosParameters"));
		return sessionRule;
	}

	@Override
	public void write(CompactWriter writer, SessionRule object) {
		writer.writeString("sessionRuleId",object.getSessionRuleId());
		writer.writeCompact("qosParameters",object.getQosParameters());
	}

	@Override
	public String getTypeName() {
		return "SessionRule";
	}

	@Override
	public Class<SessionRule> getCompactClass() {
		return SessionRule.class;
	}
}
