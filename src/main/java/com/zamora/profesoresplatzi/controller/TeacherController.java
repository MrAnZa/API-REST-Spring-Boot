package com.zamora.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.zamora.profesoresplatzi.model.Teacher;
import com.zamora.profesoresplatzi.service.TeacherService;
import com.zamora.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {
	
	@Autowired
	TeacherService _teacherService;
	
	//GET
	@RequestMapping(value="/teachers", method=RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<List<Teacher>> getTeachers(@RequestParam(value="name", required=false) String name){
		
		List<Teacher> teachers = new ArrayList<Teacher>();
		if(name==null) {
			teachers = _teacherService.findAllTeachers();
		}else {
			Teacher teacher = _teacherService.findTeacherByName(name);
			if(teacher==null) {
				return new ResponseEntity(new CustomErrorType("No Existe un Teacher con ese nombre"),HttpStatus.CONFLICT);
			}
			teachers.add(teacher);
		}
		return new ResponseEntity<List<Teacher>>(teachers,HttpStatus.OK);
	}
	
	//GET
	@RequestMapping(value = "/teachers/{id}",method = RequestMethod.GET,headers="Accept=application/json")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable("id") Long idTeacher){
		
		if(idTeacher == null || idTeacher <=0) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required"),HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.findTeacherById(idTeacher);
		if(teacher==null) {
			return new ResponseEntity(new CustomErrorType("No Existe un Teacher con ese Id"),HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<Teacher>(teacher,HttpStatus.OK);
		
	}
	//POST
	@RequestMapping(value="/teachers",method=RequestMethod.POST,headers="Accept=aplication/json")
	public ResponseEntity<?> createTeacher(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentBuilder){
		
		if(teacher.getName()==null || teacher.getName().equals("")) {
			return new ResponseEntity(new CustomErrorType("Course name is required"),HttpStatus.CONFLICT);
		}
		
		if( _teacherService.findTeacherByName(teacher.getName())!=null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		_teacherService.saveTeacher(teacher);
		
		Teacher teacher2 = _teacherService.findTeacherByName(teacher.getName());
		
		HttpHeaders headers= new HttpHeaders();
		
		headers.setLocation(uriComponentBuilder.path("/v1/teachers/{id}").buildAndExpand(teacher2.getIdTeacher()).toUri());
		
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
	}
	
	//PATCH
	@RequestMapping(value="/teachers/{id}", method = RequestMethod.PATCH, headers="Accept=application/json")
	public ResponseEntity<?> updateTeacher(@PathVariable("id") Long idTeacher, @RequestBody Teacher teacher) {
		if(idTeacher==null || idTeacher<=0) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required"),HttpStatus.CONFLICT);
		}
		Teacher currentTeacher=_teacherService.findTeacherById(idTeacher);
		if(currentTeacher==null) {
			return new ResponseEntity(new CustomErrorType("No existe un Teacher con ese id"),HttpStatus.CONFLICT);
		}
		
		currentTeacher.setName(teacher.getName());
		currentTeacher.setAvatar(teacher.getAvatar());
		_teacherService.updateTeacher(currentTeacher);
		
		return new ResponseEntity(currentTeacher,HttpStatus.OK);
	}
	
	//DELETE
	@RequestMapping(value="/teachers/{id}",method = RequestMethod.DELETE, headers="Accept=application/json")
	public ResponseEntity<?> deleteTeacher(@PathVariable("id") Long idTeacher){
		if(idTeacher==null || idTeacher <=0) {
			return new ResponseEntity(new CustomErrorType("idTeacher is required"),HttpStatus.CONFLICT);
		}
				
		Teacher teacher=_teacherService.findTeacherById(idTeacher);
		
		if(teacher==null){
			return new ResponseEntity(new CustomErrorType("No Existe un Teacher con ese id"),HttpStatus.CONFLICT);
		} 
		
		_teacherService.deleteTeacher(idTeacher);
		
		return new ResponseEntity(HttpStatus.OK);
	}
	
	public static final String TEACHER_UPLOADED_FOLDER="images/teachers/";
	//CREATE TEACHER IMAGE
	@RequestMapping(value="teachers/image",method=RequestMethod.POST,headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadTeacherImage(@RequestParam("id_teacher") Long idTeacher, @RequestParam("file") MultipartFile multipartfile, UriComponentsBuilder uriComponentBuilder){
		if(idTeacher == null) {
			return new ResponseEntity(new CustomErrorType("Please set id_Teacher"),HttpStatus.CONFLICT);
		}
		if(multipartfile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please seselect a file to upload"),HttpStatus.CONFLICT);
		}
		
		Teacher teacher = _teacherService.findTeacherById(idTeacher);
		if(teacher==null){
			return new ResponseEntity(new CustomErrorType("Teacher With id: "+ idTeacher + " Not Found"),HttpStatus.NOT_FOUND);
		}
		
		if(teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
			String filename = teacher.getAvatar();
			Path path = Paths.get(filename);
			File f = path.toFile();
			if(f.exists()) {
				f.delete();
			}
		}
		try {
			Date date = new Date();
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			 String dateName = dateformat.format(date);
			 
			 String fileName = String.valueOf(idTeacher) + "-pictureTeacher-"+ dateName + "." + multipartfile.getContentType().split("/")[1];
			 teacher.setAvatar(fileName);
			 
			 byte[] bytes = multipartfile.getBytes();
			 Path path = Paths.get(TEACHER_UPLOADED_FOLDER+ fileName);
			 Files.write(path, bytes);
			 
			 _teacherService.updateTeacher(teacher);
			 return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error During Upload"),HttpStatus.NOT_FOUND);
		}
		
	} 
}
