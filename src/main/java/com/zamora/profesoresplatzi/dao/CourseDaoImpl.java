package com.zamora.profesoresplatzi.dao;

import java.util.List;

import com.zamora.profesoresplatzi.model.Course;

public class CourseDaoImpl extends AbstractSession implements CourseDao{

	@Override
	public void saveCourse(Course course) {
		// TODO Auto-generated method stub
		getSession().persist(course);
		
	}

	@Override
	public void deleteCourse(Long idcourse) {
		// TODO Auto-generated method stub
		Course course = findById(idcourse);
		if(course!=null) {
			getSession().delete(course);
		}
	}

	@Override
	public void updateCourse(Course course) {
		// TODO Auto-generated method stub
		getSession().update(course);
	}

	@Override
	public List<Course> findAllCourses() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Course").list();
	}

	@Override
	public Course findById(Long idCourse) {
		// TODO Auto-generated method stub
		return (Course) getSession().get(Course.class, idCourse);
	}

	@Override
	public Course findByName(String name) {
		// TODO Auto-generated method stub
		return (Course) getSession().createQuery("from Course where name = :name")
				.setParameter("name", name).uniqueResult();		
	}

	@Override
	public List<Course> findByTeacher(Long idTeacher) {
		// TODO Auto-generated method stub
		return getSession().createQuery("from Course c join c.teacher t where t.idTeacher = :idTeacher")
				.setParameter("idTeacher", idTeacher).list();
	}
	
}
