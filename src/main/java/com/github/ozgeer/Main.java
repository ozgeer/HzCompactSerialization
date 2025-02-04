package com.github.ozgeer;

import java.util.HashMap;
import java.util.logging.Logger;

import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.serializer.EntrySerializer;
import com.github.ozgeer.serializer.InstructorSerialize;
import com.github.ozgeer.serializer.LectureSerializer;
import com.github.ozgeer.serializer.StudentSeriliazer;
import com.hazelcast.config.Config;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.sql.SqlResult;
import com.hazelcast.sql.SqlRow;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		Config config = new Config();
		config.getJetConfig().setEnabled(true); // sql sorgularını desteklemesi için bunun true setlenmesi gerekiyor.
		config.setClusterName("dev");
		config.getSerializationConfig()//
				.getCompactSerializationConfig() //
				.addSerializer(new StudentSeriliazer()) //
				.addSerializer(new LectureSerializer()) //
				.addSerializer(new InstructorSerialize()) //
				.addSerializer(new EntrySerializer());
		//	.addSerializer(new UUIDSerializer());

		JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		joinConfig.getMulticastConfig().setEnabled(false); // Multicast'i devre dışı bıraktık
		joinConfig.getTcpIpConfig().setEnabled(true).addMember(
				"172.17.0.3:5701"); // Docker'daki IP ve port. burada bir yanlışlık var suanda gibi. emin değilim.

		// Hazelcast instance
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

		IMap<String, Student> studentMap = hazelcastInstance.getMap("studentMap");
		studentMap.addIndex(IndexType.HASH, "name");
		IMap<String, Student> studentMap2 = hazelcastInstance.getMap("studentMapIki");
		studentMap2.addIndex(IndexType.HASH, "department");

		Instructor instructor = Instructor.of("Veli", 12);
		Instructor instructor1 = Instructor.of("Hasan", 13);

		Lecture lecture1 = Lecture.of("Math", instructor);
		Lecture lecture2 = Lecture.of("Physics", instructor1);
		HashMap<Integer, Lecture> mapOfLecture = new HashMap<>();

		// List<Student> students = List.of(new Student("ozge", 167127, "electronic", mapOfLecture), new Student("ayca", 536081, "computer", mapOfLecture));
		mapOfLecture.put(1, lecture1);
		mapOfLecture.put(2, lecture2);

		Student student = new Student("Ozge", 2, "electronic", mapOfLecture);
		Student student1 = new Student("Mavi", 3, "electronic", mapOfLecture);

		studentMap.put("sari", student);
		studentMap2.put("yesil", student1);

		String sql = "SELECT sari, this FROM demoMap WHERE department=electronic";
		try (SqlResult result = hazelcastInstance.getSql().execute(sql, 50)) {
			for (SqlRow row : result) {
				String key = row.getObject("__key"); // Anahtar
				Student studentExample = row.getObject("this"); // Değer
				System.out.println("Key: " + key + ", Student: " + studentExample);
			}
		}
		//List<Student> liste = map2.values().stream().filter(x -> x.name().equals("Mavi")).collect(Collectors.toList());

		//logger.log(Level.INFO,"Map contents: " + liste.get(0));

	}
}
