package com.zamora.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zamora.profesoresplatzi.dao.TeacherDAO;
import com.zamora.profesoresplatzi.model.Teacher;

@Service("teacherService")
@Transactional
public class TeacherServiceImpl implements TeacherService {

	@Autowired
	private TeacherDAO _teacherDAO;
	
	@Override
	public void saveTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		_teacherDAO.saveTeacher(teacher);
	}

	@Override
	public List<Teacher> findAllTeachers() {
		// TODO Auto-generated method stub
		return _teacherDAO.findAllTeachers();
	}

	@Override
	public void deleteTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		_teacherDAO.deleteTeacher(idTeacher);
	}

	@Override
	public void updateTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		_teacherDAO.updateTeacher(teacher);
	}

	@Override
	public Teacher findTeacherById(Long idTeacher) {
		// TODO Auto-generated method stub
		return _teacherDAO.findTeacherById(idTeacher);
	}

	@Override
	public Teacher findTeacherByName(String name) {
		// TODO Auto-generated method stub
		return _teacherDAO.findTeacherByName(name);
	}

}
