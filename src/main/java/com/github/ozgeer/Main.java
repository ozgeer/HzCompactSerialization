package com.github.ozgeer;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Logger;

import com.github.ozgeer.faculty.College;
import com.github.ozgeer.faculty.Faculty;
import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.utility.HazelcastInitialize;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Main {

	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws UnknownHostException {

		ClientConfig config = HazelcastInitialize.config();

		// Hazelcast instance
		HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(config);

		IMap<String, Student> studentMap = hazelcastInstance.getMap("studentMap");
		IMap<Integer, Instructor> instructorMap = hazelcastInstance.getMap("instructorMap");
		studentMap.addIndex(IndexType.HASH, "name");
		IMap<String, Student> studentMap2 = hazelcastInstance.getMap("studentMapIki");
		studentMap2.addIndex(IndexType.HASH, "department");
		IMap<Integer, Lecture> lectureMap = hazelcastInstance.getMap("lectureMap");
		lectureMap.addIndex(IndexType.HASH, "name");

		InetAddress ipAddress = InetAddress.getByName("172.30.40.12");

		Instructor instructor = Instructor.of("AHMET", "Ogut", new College("college1", URI.create("www.sau"), ipAddress));
		Instructor instructor1 = Instructor.of("CEVDET", "Yılmaz", new College("college2", URI.create("www.bau"), ipAddress));

		instructorMap.put(90, instructor);
		instructorMap.put(80, instructor1);

		Lecture lecture1 = Lecture.of("Chemistry", instructor);
		Lecture lecture2 = Lecture.of("Physics", instructor1);

		lectureMap.put(10, lecture1);
		lectureMap.put(20, lecture2);
		HashMap<Integer, Lecture> mapOfLecture = new HashMap<>();

		// List<Student> students = List.of(new Student("ozge", 167127, "electronic", mapOfLecture), new Student("ayca", 536081, "computer", mapOfLecture));
		mapOfLecture.put(1, lecture1);
		mapOfLecture.put(2, lecture2);

		Student student = new Student("Ozge", 2, Faculty.ENGINEERING, "industry", mapOfLecture, new College("SAU", URI.create("www.sau"), ipAddress));
		Student student1 = new Student("Mehmet", 3, Faculty.ENGINEERING, "chemistry", mapOfLecture, new College("SUBU", URI.create("www" + ".bau"), ipAddress));

		studentMap.put("sari1", student);
		studentMap.put("yesil1", student1);

		HazelcastInitialize.Query(hazelcastInstance);
		//		Set<String>  keys = studentMap.keySet();
		//		for (String item : keys) {
		//			System.out.println(studentMap.get(item));
		//		}
		for (Student item : studentMap.values()) {
			System.out.println("-----------------------------------------------------------------------------------------");
			System.out.println(item.toString());
		}

	}

		//List<Student> liste = map2.values().stream().filter(x -> x.name().equals("Mavi")).collect(Collectors.toList());

		//logger.log(Level.INFO,"Map contents: " + liste.get(0));

}

