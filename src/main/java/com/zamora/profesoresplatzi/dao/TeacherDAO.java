package com.zamora.profesoresplatzi.dao;

import java.util.List;

import com.zamora.profesoresplatzi.model.Teacher;

public interface TeacherDAO {
	

	void saveTeacher(Teacher teacher);
	
	List<Teacher> findAllTeachers();
	
	void deleteTeacher(Long idTeacher);
	
	void updateTeacher(Teacher teacher);

	Teacher findTeacherById(Long idTeacher);
	
	Teacher findTeacherByName(String name);
}
