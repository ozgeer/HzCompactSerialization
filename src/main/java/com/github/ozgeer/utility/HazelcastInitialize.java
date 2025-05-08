package com.github.ozgeer.utility;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.github.ozgeer.serializer.InstructorSerializer;
import com.github.ozgeer.serializer.EntrySerializer;
import com.github.ozgeer.serializer.FacultySerializer;

import org.springframework.stereotype.Component;

import com.github.ozgeer.Main;
import com.github.ozgeer.faculty.Faculty;
import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.smf.model.BindingParameters;
import com.github.ozgeer.smf.model.QosParameters;
import com.github.ozgeer.smf.model.SessionRule;
import com.github.ozgeer.smf.serializer.BindingParametersSerializer;
import com.github.ozgeer.smf.serializer.QosParametersSerializer;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.CompactSerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.BiTuple;
import com.hazelcast.nio.serialization.HazelcastSerializationException;
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
		networkConfig.addAddress("172.30.40.164:5701"); // Use the HOST IP address of the Docker container
		// Optional: configure timeouts, retries, etc.
		networkConfig.setSmartRouting(true);
		networkConfig.setConnectionTimeout(5000);

		//		compactConfig.addClass(Student.class);
		//		compactConfig.addClass(Lecture.class);
		//		compactConfig.addClass(College.class);
		//
		//		compactConfig.addSerializer(new InstructorSerializer()) //
		//				.addSerializer(new EntrySerializer()) //
		//				.addSerializer(new FacultySerializer());

		if (compactSerialization) {
			Set<Class<?>> selectedTypes = new HashSet<>();

			for (Class<?> clazz : CLAZZES_FOR_COMPACT_REGISTRATION) {
				addClazzToCompactSerialization(clazz, selectedTypes);
			}

			CompactSerializationConfig compactConfig = clientConfig.getSerializationConfig().getCompactSerializationConfig();
			for (Supplier<CompactSerializer> customSerializer : CUSTOM_SERIALIZED_CLASSES.values()) {
				compactConfig.addSerializer(customSerializer.get());
			}

			for (Class<?> type : selectedTypes) {
				if (!CUSTOM_SERIALIZED_CLASSES.containsKey(type)) {
					compactConfig.addClass(type);
				}
			}
		}
		return clientConfig;
	}

	public static void addClazzToCompactSerialization(Class<?> clazzToCheck, Set<Class<?>> selectedTypes) {
		selectedTypes.add(clazzToCheck);

		Set<Field> fieldsToCheck = getAllFields(new HashSet<>(), clazzToCheck);

		Set<Field> newFieldsToCheck;
		do {
			newFieldsToCheck = filterFieldsToAddCompactSerialization(fieldsToCheck, selectedTypes);
			fieldsToCheck.clear();
			fieldsToCheck.addAll(newFieldsToCheck);
		} while (!newFieldsToCheck.isEmpty());
	}

	private static Set<Field> getAllFields(Set<Field> clazzFields, Class<?> type) {
		Collections.addAll(clazzFields, type.getDeclaredFields());
		if (type.getSuperclass() != null && type.getSuperclass() != Object.class) {
			getAllFields(clazzFields, type.getSuperclass());
		}
		return clazzFields;
	}

	public static Set<Field> filterFieldsToAddCompactSerialization(Set<Field> fieldsToCheck, Set<Class<?>> selectedTypes) {
		Set<Field> newFieldsToCheck = new HashSet<>();
		for (Iterator i = fieldsToCheck.iterator(); i.hasNext(); ) {
			Field f = (Field) i.next();
			Set<Class<?>> typesOfField = new HashSet<>();

			if (!checkField(f)) {
				continue;
			}

			Class<?> type = f.getType();
			if (!registerForCompactSerialization(type)) {
				continue;
			} else if (type.isArray()) {
				if (registerForCompactSerialization(type.getComponentType()))
					typesOfField.add(type.getComponentType());
			} else if (List.class.equals(type) || Set.class.equals(type)) {
				if (registerForCompactSerialization(getSingleComponentType(f.getGenericType())))
					typesOfField.add(getSingleComponentType(f.getGenericType()));
			} else if (Map.class.equals(type) || HashMap.class.equals(type)) {
				BiTuple<Class<?>, Class<?>> componentTypes = null;
				try {
					componentTypes = getTupleComponentTypes(f.getGenericType());
				} catch (HazelcastSerializationException e) {
					if (CUSTOM_SERIALIZED_CLASSES.containsKey(f.getDeclaringClass())) {
						//						nloggerPmStart.atDebug(Marks.IMDG.marker).log("C.FNAME = {} , C.PCLASS = {} , C.FTYPE = {} , C.STYPES = {}", f.getName(),
						//								f.getDeclaringClass().getSimpleName(), f.getType().getSimpleName(), typesOfField);

						logger.info("mapte sorun var.");
						continue;
					}

					throw e;
				}
				if (registerForCompactSerialization(componentTypes.element1)) {
					typesOfField.add(componentTypes.element1);
				}
				if (registerForCompactSerialization(componentTypes.element2)) {
					typesOfField.add(componentTypes.element2);
				}
			} else {
				typesOfField.add(type);
			}

			if (!typesOfField.isEmpty()) {
				for (Class<?> t : typesOfField) {
					if (selectedTypes.add(t)) {
						Set<Field> declaredFields = getAllFields(new HashSet<>(), t);
						declaredFields.addAll(newFieldsToCheck);
					}
				}
			}
		} // end of fields

		return newFieldsToCheck;
	}

	public static boolean checkField(Field f) {
		boolean needSerialization = true;

		if (Modifier.isStatic(f.getModifiers()) || Modifier.isTransient(f.getModifiers())) {
			needSerialization = false;
		}

		return needSerialization;
	}

	private static boolean registerForCompactSerialization(Class<?> type) {
		boolean registerForZeroConfigSerialization = true;

		if (TYPES_TO_ID.containsKey(type) || type.isEnum()) {
			registerForZeroConfigSerialization = false;
		}

		return registerForZeroConfigSerialization;
	}

	public static Class<?> getSingleComponentType(Type genericType) {
		if (!(genericType instanceof ParameterizedType)) {// List,Set veya Map mi diye kontrol ediyor. runtime da parameterized ile temsil ediliyorlar.
			throw new HazelcastSerializationException("It is required that the type " + genericType + " must be parameterized.");
		}

		ParameterizedType parameterizedType = (ParameterizedType) genericType;
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		if (typeArguments.length != 1) { //map olup olmadıgını kontrol ediyor
			throw new HazelcastSerializationException("Expected type " + genericType + " to have a single type argument.");
		}

		Type typeArgument = typeArguments[0];
		if (!(typeArgument instanceof Class)) {
			throw new HazelcastSerializationException("Expected type argument of type " + genericType + " to be a class");
		}

		return (Class<?>) typeArgument;
	}

	public static BiTuple<Class<?>, Class<?>> getTupleComponentTypes(Type genericType) {
		if (!(genericType instanceof ParameterizedType)) {
			throw new HazelcastSerializationException("Expected the type " + genericType + " to be parameterized.");
		}

		ParameterizedType parameterizedType = (ParameterizedType) genericType;
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		if (typeArguments.length != 2) {
			throw new HazelcastSerializationException("Expected type " + genericType + " to have two type arguments.");
		}

		Type keyTypeArgument = typeArguments[0];
		Type valueTypeArgument = typeArguments[1];
		if (!(keyTypeArgument instanceof Class) || !(valueTypeArgument instanceof Class)) {
			throw new HazelcastSerializationException("Expected type arguments of type " + genericType + " to be classes");
		}

		return BiTuple.of((Class<?>) keyTypeArgument, (Class<?>) valueTypeArgument);
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
		CLAZZES_FOR_COMPACT_REGISTRATION.add(SessionRule.class);

		CUSTOM_SERIALIZED_CLASSES.put(Instructor.class, InstructorSerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(Entry.class, EntrySerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(Faculty.class, FacultySerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(BindingParameters.class, BindingParametersSerializer::new);
		CUSTOM_SERIALIZED_CLASSES.put(QosParameters.class,QosParametersSerializer::new);
	}
}





