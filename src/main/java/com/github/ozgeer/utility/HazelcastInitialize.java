package com.github.ozgeer.utility;

import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.github.ozgeer.Main;
import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.serializer.EntrySerializer;
import com.github.ozgeer.serializer.InstructorSerialize;
import com.github.ozgeer.serializer.LectureSerializer;
import com.github.ozgeer.serializer.StudentSeriliazer;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;

@Component
public class HazelcastInitialize {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	public static Config config() {
		Config config = new Config();
		config.getJetConfig().setEnabled(true); // sql sorgularını desteklemesi için bunun true setlenmesi gerekiyor.
		config.setClusterName("dev");
		config.getSerializationConfig()//
				.getCompactSerializationConfig() //
				.addSerializer(new StudentSeriliazer()) //
				.addSerializer(new LectureSerializer()) //
				.addSerializer(new InstructorSerialize()) //
				.addSerializer(new EntrySerializer()) //
				.addSerializer(new org.example.serializer.UUIDSerializer());

		JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		joinConfig.getMulticastConfig().setEnabled(false); // Multicast'i devre dışı bıraktık
		joinConfig.getTcpIpConfig().setEnabled(true).addMember(
				"172.17.0.3:5701"); // Docker'daki IP ve port. burada bir yanlışlık var suanda gibi. emin değilim.
		return config;
	}

	public static void Query(HazelcastInstance hazelcastInstance) {
		// CREATE MAPPING
		String createInstructorMappingQuery = """
				    CREATE OR REPLACE EXTERNAL MAPPING "hazelcast"."public"."instructorMap"\s
				        EXTERNAL NAME "instructorMap" (
				          "__key" INTEGER ,
				          "name" VARCHAR EXTERNAL NAME "this.name",
				          "no" INTEGER EXTERNAL NAME "this.no"
				        )
				        TYPE "IMap"
				        OPTIONS (
				          'keyFormat'='java',
				          'keyJavaClass'='java.lang.Integer',
				          'valueFormat'='compact',
				          'valueCompactTypeName'='instructor'
				        );
				""";

		// Mapping'i oluştur
		try (SqlResult result = hazelcastInstance.getSql().execute(createInstructorMappingQuery)) {
			System.out.println("Instructor Mapping created successfully.");
		}

		String sql = "SELECT this FROM instructorMap WHERE name='Veli'";
		try (SqlResult result = hazelcastInstance.getSql().execute(sql)) {
			for (SqlRow row : result) {
				//	String key = row.getObject("__key"); // key
				Instructor studentExample = row.getObject(0); //value
				System.out.println("Instructor: " + studentExample);
			}
		}

		String createLectureMappingQuery = """
				    CREATE OR REPLACE EXTERNAL MAPPING "hazelcast"."public"."lectureMap"\s
				        EXTERNAL NAME "lectureMap" (
				          "__key" INTEGER ,
				          "name" VARCHAR EXTERNAL NAME "this.name",
				          "instructor_name" VARCHAR EXTERNAL NAME "instructor_name",
				          "instructor_no" INT EXTERNAL NAME "instructor_no"
				        )
				        TYPE "IMap"
				        OPTIONS (
				          'keyFormat'='java',
				          'keyJavaClass'='java.lang.Integer',
				          'valueFormat'='compact',
				          'valueCompactTypeName'='lecture'
				        );
				""";

		try (SqlResult lectureResult = hazelcastInstance.getSql().execute(createLectureMappingQuery)) {
			System.out.println("Lecture Mapping created successfully.");
		}

		String lectureSql = "SELECT this FROM lectureMap WHERE name='Math'";

		try (SqlResult lectureResult = hazelcastInstance.getSql().execute(lectureSql)) {
			for (SqlRow row : lectureResult) {
				Lecture lecture = row.getObject(0);
				Instructor instructor_name = lecture.instructor();
				System.out.println(lecture);
			}
		}
		}
	}

