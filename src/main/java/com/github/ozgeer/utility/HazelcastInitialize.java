package com.github.ozgeer.utility;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.github.ozgeer.Main;
import com.github.ozgeer.faculty.College;
import com.github.ozgeer.faculty.Faculty;
import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.serializer.CollegeSerializer;
import com.github.ozgeer.serializer.EntrySerializer;
import com.github.ozgeer.serializer.FacultySerializer;
import com.github.ozgeer.serializer.InstructorSerializer;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.CompactSerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;

@Component
public class HazelcastInitialize {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	public static Set<Class<?>> CLAZZES_FOR_COMPACT_REGISTRATION = new HashSet<>();
	public static Map<Class<?>, Supplier<CompactSerializer>> CUSTOM_SERIALIZED_CLASSES = new HashMap<>();

	public static Map<Class<?>, Integer> TYPES_TO_ID = new HashMap<>();

	public static Map<Class<?>, BiFunction<CompactReader,String,Object>> READERS = new HashMap<>();

	public static boolean compactSerialization = true;

	public static ClientConfig config() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setClusterName("hz-node-on-docker");
		clientConfig.getSerializationConfig().setEnableCompression(true);

		ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
		networkConfig.addAddress("10.22.216.243:5701"); // Use the HOST IP address of the Docker container
		// Optional: configure timeouts, retries, etc.
		networkConfig.setSmartRouting(true);
		networkConfig.setConnectionTimeout(5000);

		CompactSerializationConfig compactConfig = clientConfig.getSerializationConfig().getCompactSerializationConfig();

		compactConfig.addClass(Student.class);
		compactConfig.addClass(Lecture.class);
		compactConfig.addClass(College.class);

		compactConfig.addSerializer(new InstructorSerializer()) //
				.addSerializer(new EntrySerializer()) //
				.addSerializer(new FacultySerializer());
		return clientConfig;

	}



	public static void Query(HazelcastInstance hazelcastInstance) {
		// CREATE MAPPING FOR INSTRUCTOR
		String createInstructorMappingQuery = """
				    CREATE OR REPLACE EXTERNAL MAPPING "hazelcast"."public"."instructorMap"\s
				        EXTERNAL NAME "instructorMap" (
				          "__key" INTEGER ,
				          "name" VARCHAR EXTERNAL NAME "this.name",
				          "no" INTEGER EXTERNAL NAME "this.no",
				          "surname" VARCHAR EXTERNAL NAME "this.surname"
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

		String sql = "SELECT this FROM instructorMap WHERE name='CEVDET'";
		try (SqlResult result = hazelcastInstance.getSql().execute(sql)) {
			for (SqlRow row : result) {
				Instructor instructor = row.getObject(0); //value
				logger.info("Instructor: " + instructor);
				logger.info("college:" + instructor.getCollege());
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
				Instructor instructor_name = lecture.getInstructor();
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

	static {
		READERS.put(Byte.TYPE, CompactReader::readInt8);
		READERS.put(Character.TYPE, CompactReader::readInt16);
		READERS.put(Short.TYPE, CompactReader::readInt16);
		READERS.put(Integer.TYPE, CompactReader::readInt32);
		READERS.put(Long.TYPE, CompactReader::readInt64);
		READERS.put(Float.TYPE, CompactReader::readFloat32);
		READERS.put(Double.TYPE, CompactReader::readFloat64);
		READERS.put(Boolean.TYPE, CompactReader::readBoolean);
		READERS.put(String.class, CompactReader::readString);
		READERS.put(BigDecimal.class, CompactReader::readDecimal);
		READERS.put(LocalTime.class, CompactReader::readTime);
		READERS.put(LocalDate.class, CompactReader::readDate);
		READERS.put(LocalDateTime.class, CompactReader::readTimestamp);
		READERS.put(OffsetDateTime.class, CompactReader::readTimestampWithTimezone);
		READERS.put(Boolean.class, CompactReader::readNullableBoolean);
		READERS.put(Byte.class, CompactReader::readNullableInt8);
		READERS.put(Character.class, CompactReader::readNullableInt16);
		READERS.put(Short.class, CompactReader::readNullableInt16);
		READERS.put(Integer.class, CompactReader::readNullableInt32);
		READERS.put(Long.class, CompactReader::readNullableInt64);
		READERS.put(Float.class, CompactReader::readNullableFloat32);
		READERS.put(Double.class, CompactReader::readNullableFloat64);
	}

	static {
		Set<Class<?>> clazzes = READERS.keySet();
		Integer typeId = 1;
		for (Class<?> clazz : clazzes) {
			TYPES_TO_ID.put(clazz, typeId);
			typeId++;
		}

		//Below types conflict with HZ basic serializer. We implement custom serialization for these types in AbstractSerializer
		TYPES_TO_ID.put(Date.class, -1);
		TYPES_TO_ID.put(UUID.class, -2);
		TYPES_TO_ID.put(Array.newInstance(Byte.TYPE, 0).getClass(), -3); //Byte array

		Class<?> singleArrayInt = Array.newInstance(Integer.TYPE, 0).getClass();
		TYPES_TO_ID.put(Array.newInstance(singleArrayInt, 0).getClass(), -4); //Two dimensional int array
		CLAZZES_FOR_COMPACT_REGISTRATION.add(Student.class);

		CUSTOM_SERIALIZED_CLASSES.put(College.class, CollegeSerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(Instructor.class, InstructorSerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(Entry.class, EntrySerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(Faculty.class, FacultySerializer::new);
	}
}





