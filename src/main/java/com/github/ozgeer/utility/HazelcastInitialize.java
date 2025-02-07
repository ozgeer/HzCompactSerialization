package com.github.ozgeer.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.github.ozgeer.Main;
import com.github.ozgeer.faculty.Faculty;
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
		config.getJetConfig().setEnabled(true); // shall adjust as true to support sql queries.
		config.setClusterName("dev");
		config.getSerializationConfig()//
				.getCompactSerializationConfig() //
				.addSerializer(new StudentSeriliazer()) //
				.addSerializer(new LectureSerializer()) //
				.addSerializer(new InstructorSerialize()) //
				.addSerializer(new EntrySerializer())//
				.addClass(Faculty.class);
		//		.addSerializer(new org.example.serializer.UUIDSerializer());

		JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		joinConfig.getMulticastConfig().setEnabled(false); // disabled the multicasting
		joinConfig.getTcpIpConfig().setEnabled(true).addMember("172.17.0.3:5701"); // IP and port information on Docker.
		return config;
	}

	public static void Query(HazelcastInstance hazelcastInstance) {
		// CREATE MAPPING FOR INSTRUCTOR
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

		// Logging
		try (SqlResult result = hazelcastInstance.getSql().execute(createInstructorMappingQuery)) {
			logger.info("Instructor mapping created successfully.");
		}

		String sql = "SELECT this FROM instructorMap WHERE name='Veli'";
		try (SqlResult result = hazelcastInstance.getSql().execute(sql)) {
			for (SqlRow row : result) {
				Instructor studentExample = row.getObject(0); //value
				logger.info("Instructor: " + studentExample);
			}
		}
		// CREATE MAPPING FOR LECTURE
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
		// Logging
		try (SqlResult lectureResult = hazelcastInstance.getSql().execute(createLectureMappingQuery)) {
			logger.info("Lecture mapping created successfully.");
		}

		String lectureSql = "SELECT this FROM lectureMap WHERE name='Math'";

		try (SqlResult lectureResult = hazelcastInstance.getSql().execute(lectureSql)) {
			for (SqlRow row : lectureResult) {
				Lecture lecture = row.getObject(0);
				Instructor instructor_name = lecture.instructor();
				logger.info(String.valueOf(lecture));
			}
		}
		//CREATE MAPPING FOR STUDENT
		String createStudentMappingQuery = """
				    CREATE OR REPLACE EXTERNAL MAPPING "hazelcast"."public"."studentMap"\s
				        EXTERNAL NAME "studentMap" (
				          "__key" INTEGER ,
				          "name" VARCHAR EXTERNAL NAME "this.name",
				          "no" INTEGER EXTERNAL NAME "this.no",
				          "faculty" VARCHAR EXTERNAL NAME "this.faculty"
				        )
				        TYPE "IMap"
				        OPTIONS (
				          'keyFormat'='java',
				          'keyJavaClass'='java.lang.Integer',
				          'valueFormat'='compact',
				          'valueCompactTypeName'='student'
				        );
								
				""";
		//Logging
		try (SqlResult studentResult = hazelcastInstance.getSql().execute(createStudentMappingQuery)) {
			logger.info("Student mapping created successfully.");
		}

		String studentSql = "SELECT name,faculty FROM studentMap";

		try (SqlResult studentResult = hazelcastInstance.getSql().execute(studentSql)) {
			for(SqlRow row : studentResult){
				String name = row.getObject("name");
				String facultyName = row.getObject("faculty");
				logger.log(Level.INFO,name + "," + facultyName);


			}
		}
	}
}

