package com.zamora.profesoresplatzi.service;

import java.util.List;

import com.zamora.profesoresplatzi.model.Teacher;

public interface TeacherService {
	
	void saveTeacher(Teacher teacher);
	
	List<Teacher> findAllTeachers();
	
	void deleteTeacher(Long idTeacher);
	
	void updateTeacher(Teacher teacher);

	Teacher findTeacherById(Long idTeacher);
	
	Teacher findTeacherByName(String name);
}
