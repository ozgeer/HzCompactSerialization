package com.github.ozgeer.faculty;

import java.util.HashMap;

public record Student(String name, int no, Faculty faculty, String department, HashMap<Integer, Lecture> lectureHashMap) {


}
