package com.zamora.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import com.zamora.profesoresplatzi.model.Course;
import com.zamora.profesoresplatzi.service.CourseService;
import com.zamora.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class CourseController {
	@Autowired
	CourseService _courseService;
	
	//GET
	@RequestMapping(value="/courses",method = RequestMethod.GET, headers ="Accept=application/json")
	public ResponseEntity<List<Course>> getCourses(@RequestParam(value="name", required=false) String name,@RequestParam(value="teacher", required=false) Long idTeacher){
		
		List<Course> courses = new ArrayList();
		if(name == null && idTeacher == null) {
			courses = _courseService.findAllCourses();
		
			if(courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}else {
				return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
			}
			
		}else if(idTeacher != null){
			
			courses = _courseService.findByTeacher(idTeacher);
			if(courses.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			
		}
			else if (name != null){
				Course course = _courseService.findByName(name);
				if(course == null) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
				}
				courses.add(course);
				
			}
		return new ResponseEntity<List<Course>>(courses,HttpStatus.OK);
	}
	//GET
	@RequestMapping(value="/courses/{id}",method = RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<Course> getCourseById(@PathVariable("id") Long idCourse){
		if(idCourse == null || idCourse <=0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required"),HttpStatus.CONFLICT);
		}
		Course course = _courseService.findById(idCourse);
		
		if(course==null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Course>(course,HttpStatus.OK);
	}
	
	//POST
	@RequestMapping(value="/courses",method=RequestMethod.POST,headers="Accept=application/json")
	public ResponseEntity<?> createCourse(@RequestBody Course course, UriComponentsBuilder uriComponentBuilder){
		if(course.getName().equals(null) || course.getName().isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Course name is required"),HttpStatus.CONFLICT);
		}
		
		if(_courseService.findByName(course.getName())!=null) {
			return new ResponseEntity(new CustomErrorType("El nombre del curso ya esta Registrado, No se haga la ¡VISTIMA!"),HttpStatus.CONFLICT);
		}
		
		_courseService.saveCourse(course);
		Course course2 = _courseService.findByName(course.getName());
		HttpHeaders headers= new HttpHeaders();
		
		headers.setLocation(uriComponentBuilder.path("/v1/courses/{id}").buildAndExpand(course2.getIdCourse()).toUri());
		
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
	}
	//PATCH
	@RequestMapping(value="/courses/{id}",method=RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<?> updateCourse(@PathVariable("id") Long idCourse, @RequestBody Course course){
		
		Course currentCourse = _courseService.findById(idCourse);
		
		if(idCourse == null || idCourse <=0) {
			return new ResponseEntity(new CustomErrorType("idCourse is required"),HttpStatus.CONFLICT);
		}
		
		if(currentCourse==null) {
			return new ResponseEntity(new CustomErrorType("No existe un Course con ese id"),HttpStatus.CONFLICT);
		}
		currentCourse.setName(course.getName());
		currentCourse.setProject(course.getProject());
		currentCourse.setThemes(course.getThemes());
		_courseService.updateCourse(currentCourse);
		
		return new ResponseEntity<Course>(currentCourse,HttpStatus.OK);
		
	}
	//DELETE
	@RequestMapping(value="/courses/{id}",method = RequestMethod.DELETE,headers="Accept=application/json")
	public ResponseEntity<?> deleteCourse(@PathVariable("id") Long idCourse) {
		
		if(idCourse==null || idCourse <=0 ) {
			return new ResponseEntity(new CustomErrorType("idCourse is required"),HttpStatus.CONFLICT);
		}
		
		Course course =_courseService.findById(idCourse);
		
		if(course==null) {
			return new ResponseEntity(new CustomErrorType("No existe un Course con ese id"),HttpStatus.CONFLICT);
		}
		_courseService.deleteCourse(idCourse);
		
		return new ResponseEntity(HttpStatus.OK);
	}
}
