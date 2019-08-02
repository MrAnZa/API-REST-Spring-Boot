package com.zamora.profesoresplatzi.dao;

import java.util.Iterator;
import java.util.List;

import com.zamora.profesoresplatzi.model.Teacher;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;


public class TeacherDAOImpl extends AbstractSession implements TeacherDAO{


	public void saveTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		getSession().persist(teacher);		
	}

	public List<Teacher> findAllTeachers() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Teacher").list();
	}

	public void deleteTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		Teacher teacher=findTeacherById(idTeacher);
		if(teacher!=null) {
			Iterator<TeacherSocialMedia> i = teacher.getTeachersocialmedias().iterator();
			while(i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				i.remove();
				getSession().delete(teacherSocialMedia);
			}
			teacher.getTeachersocialmedias()
.clear();			getSession().delete(teacher);
		}
	}

	public void updateTeacher(Teacher teacher) {
		// TODO Auto-generated method stub
		getSession().update(teacher);
	}

	public Teacher findTeacherById(Long idTeacher) {
		// TODO Auto-generated method stub
		return (Teacher) getSession().get(Teacher.class, idTeacher);
	}

	public Teacher findTeacherByName(String name) {
		// TODO Auto-generated method stub
		return (Teacher) getSession().createQuery("from Teacher where name = :name")
				.setParameter("name", name).uniqueResult();
		
	}

}
