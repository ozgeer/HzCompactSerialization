package com.github.ozgeer.Faculty;

import java.util.HashMap;

public record Student(String name, int no, String department, HashMap<Integer,Lecture> lectureHashMap) {


}
