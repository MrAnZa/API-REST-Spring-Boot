package com.zamora.profesoresplatzi.service;

import java.util.List;

import com.zamora.profesoresplatzi.model.Course;

public interface CourseService {
	
	void saveCourse(Course course);
	
	void deleteCourse(Long idcourse);
	
	void updateCourse(Course course);
	
	List<Course> findAllCourses();
	
	Course findById(Long idCourse);
	
	Course findByName(String name);
	
	List<Course> findByTeacher(Long idTeacher);
}
