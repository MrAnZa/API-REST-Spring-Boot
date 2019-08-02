package com.zamora.profesoresplatzi.dao;

import java.util.List;

import com.zamora.profesoresplatzi.model.Course;

public interface CourseDao {
	
	void saveCourse(Course course);
	
	void deleteCourse(Long idcourse);
	
	void updateCourse(Course course);
	
	List<Course> findAllCourses();
	
	Course findById(Long idCourse);
	
	Course findByName(String name);
	
	List<Course> findByTeacher(Long idTeacher);
}
