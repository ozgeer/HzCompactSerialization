package com.github.ozgeer.faculty;

import java.util.HashMap;

public record Student(String name, int no, String department, HashMap<Integer,Lecture> lectureHashMap) {


}
