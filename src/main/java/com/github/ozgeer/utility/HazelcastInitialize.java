package com.github.ozgeer.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.sql.SqlResult;

@Component
public class HazelcastInitialize implements CommandLineRunner {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Override
	public void run(String... args) throws Exception {
		// CREATE MAPPING komutunu tanımla
		String createMappingQuery = """
				    CREATE OR REPLACE EXTERNAL MAPPING "hazelcast"."public"."demoMap"
				    EXTERNAL NAME "demoMap"
				    (
				      "__key" VARCHAR EXTERNAL NAME "__key",
				      "department" VARCHAR EXTERNAL NAME "this.department",
				      "lectures" OBJECT EXTERNAL NAME "this.lectures",
				      "name" VARCHAR EXTERNAL NAME "this.name",
				      "no" INTEGER EXTERNAL NAME "this.no"
				    )
				    TYPE "IMap"
				    OPTIONS (
				      'keyFormat'='java',
				      'keyJavaClass'='java.lang.String',
				      'valueFormat'='compact',
				      'valueCompactTypeName'='student'
				    );
				""";

		// Mapping'i oluştur
		try (SqlResult result = hazelcastInstance.getSql().execute(createMappingQuery)) {
			System.out.println("Mapping created successfully.");
		}
	}
}
