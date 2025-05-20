package com.github.ozgeer.smf.serializer;

import java.net.URI;
import java.net.URISyntaxException;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class URISerializer implements CompactSerializer<URI> {
		@Override
		public URI read(CompactReader reader) {
			String uriString = reader.readString("uri");
			try {
				return new URI(uriString);
			} catch (URISyntaxException e) {
				throw new RuntimeException("Failed to deserialize URI", e);
			}
		}

		@Override
		public void write(CompactWriter writer, URI uri) {
			writer.writeString("uri", uri.toString());
		}

		@Override
		public String getTypeName() {
			return "uri";
		}

		@Override
		public Class<URI> getCompactClass() {
			return URI.class;
		}
	}

