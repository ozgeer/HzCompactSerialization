package com.github.ozgeer;

import java.util.HashMap;
import java.util.logging.Logger;

import com.github.ozgeer.faculty.Faculty;
import com.github.ozgeer.faculty.Instructor;
import com.github.ozgeer.faculty.Lecture;
import com.github.ozgeer.faculty.Student;
import com.github.ozgeer.utility.HazelcastInitialize;
import com.hazelcast.config.Config;
import com.hazelcast.config.IndexType;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {

		Config config = HazelcastInitialize.config();

		// Hazelcast instance
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

		IMap<String, Student> studentMap = hazelcastInstance.getMap("studentMap");
		IMap<Integer, Instructor> instructorMap = hazelcastInstance.getMap("instructorMap");
		studentMap.addIndex(IndexType.HASH, "name");
		IMap<String, Student> studentMap2 = hazelcastInstance.getMap("studentMapIki");
		studentMap2.addIndex(IndexType.HASH, "department");
		IMap<Integer, Lecture> lectureMap = hazelcastInstance.getMap("lectureMap");
		lectureMap.addIndex(IndexType.HASH, "name");

		Instructor instructor = Instructor.of("Veli", 12);
		Instructor instructor1 = Instructor.of("Hasan", 13);

		instructorMap.put(90, instructor);
		instructorMap.put(80, instructor1);

		Lecture lecture1 = Lecture.of("Math", instructor);
		Lecture lecture2 = Lecture.of("Physics", instructor1);

		lectureMap.put(10, lecture1);
		lectureMap.put(20, lecture2);
		HashMap<Integer, Lecture> mapOfLecture = new HashMap<>();

		// List<Student> students = List.of(new Student("ozge", 167127, "electronic", mapOfLecture), new Student("ayca", 536081, "computer", mapOfLecture));
		mapOfLecture.put(1, lecture1);
		mapOfLecture.put(2, lecture2);

		Student student = new Student("Ozge", 2, Faculty.ENGINEERING, "electronic", mapOfLecture);
		Student student1 = new Student("Mavi", 3, Faculty.ENGINEERING, "electronic", mapOfLecture);

		studentMap.put("sari", student);
		studentMap2.put("yesil", student1);

		HazelcastInitialize.Query(hazelcastInstance);
	}

		//List<Student> liste = map2.values().stream().filter(x -> x.name().equals("Mavi")).collect(Collectors.toList());

		//logger.log(Level.INFO,"Map contents: " + liste.get(0));

	}

