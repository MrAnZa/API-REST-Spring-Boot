package com.zamora.profesoresplatzi.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
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

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.Teacher;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;
import com.zamora.profesoresplatzi.service.SocialMediaService;
import com.zamora.profesoresplatzi.service.TeacherService;
import com.zamora.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class TeacherController {
	
	@Autowired
	private TeacherService _teacherService;
	
	@Autowired
	private SocialMediaService  _socialMediaService;
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
		
		if(!teacher.getAvatar().isEmpty() || teacher.getAvatar() != null) {
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
			 teacher.setAvatar(TEACHER_UPLOADED_FOLDER + fileName);
			 
			 byte[] bytes = multipartfile.getBytes();
			 Path path = Paths.get(TEACHER_UPLOADED_FOLDER + fileName);
			 Files.write(path, bytes);
			 
			 _teacherService.updateTeacher(teacher);
			 return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error During Upload"),HttpStatus.NOT_FOUND);
		}
		
	} 
	//GET IMAGE
	@RequestMapping(value="/teachers/{id_teacher}/images",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		
		if(idTeacher==null) {
		return new ResponseEntity(new CustomErrorType("Please set id_teacher"),HttpStatus.CONFLICT);
		}
		
		Teacher teacher= _teacherService.findTeacherById(idTeacher);
		
		if(teacher == null) {
			return new ResponseEntity(new CustomErrorType("Teacher With id: "+ idTeacher + " Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File f = path.toFile();
			if(!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Image Not Found"),HttpStatus.NOT_FOUND);
			}
			byte[] image= Files.readAllBytes(path);
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
		
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image"),HttpStatus.CONFLICT);
		}
	}
	//DELETE
	@RequestMapping(value="/teachers/{id_teacher}/images",method=RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteTeacherImage(@PathVariable("id_teacher") Long idTeacher){
		if(idTeacher==null) {
			return new ResponseEntity(new CustomErrorType("Please set id_teacher"),HttpStatus.CONFLICT);
			}
			
			Teacher teacher= _teacherService.findTeacherById(idTeacher);
			
			if(teacher == null) {
				return new ResponseEntity(new CustomErrorType("Teacher With id: "+ idTeacher + " Not Found"),HttpStatus.NOT_FOUND);
			}
			
			if(teacher.getAvatar().isEmpty() || teacher.getAvatar() == null){
				return new ResponseEntity(new CustomErrorType("This teacher doesn`t have image assigned"),HttpStatus.NOT_FOUND);
			}
			
			String fileName = teacher.getAvatar();
			Path path = Paths.get(fileName);
			File file=path.toFile();
			if(file.exists()) {
				file.delete();
			}
			
			teacher.setAvatar("");
			_teacherService.updateTeacher(teacher);
			
			return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
	}
	
	@RequestMapping(value="teachers/socialMedias", method=RequestMethod.PATCH,headers="Accept=application/json")
	public ResponseEntity<?> assignTeacherSocialMedia(@RequestBody Teacher teacher, UriComponentsBuilder uriComponentBuilder) {
		if(teacher.getIdTeacher() == null) {
			return new ResponseEntity(new CustomErrorType("We need almost id_teacher,id_social_media and nickname"),HttpStatus.CONFLICT);	
		}
		
		Teacher teacherSaved = _teacherService.findTeacherById(teacher.getIdTeacher());
		
		if(teacherSaved==null) {
			return new ResponseEntity(new CustomErrorType("Teacher With id: "+ teacher.getIdTeacher() + " Not Found"),HttpStatus.NOT_FOUND);
		}
		//tests
		//return new ResponseEntity<Teacher>(teacher,HttpStatus.OK);
		//
		
		if(teacher.getTeachersocialmedias().size()==0) {
			return new ResponseEntity(new CustomErrorType("We need almost id_teacher,id_social_media and nickname"),HttpStatus.CONFLICT);
		}else {
			Iterator<TeacherSocialMedia> i = teacher.getTeachersocialmedias().iterator();
			while(i.hasNext()) {
				TeacherSocialMedia teacherSocialMedia = i.next();
				if(teacherSocialMedia.getSocialMedia().getIdSocialMedia()==null || teacherSocialMedia.getNickname()==null) {
					return new ResponseEntity(new CustomErrorType("We need almost id_teacher,id_social_media and nickname"),HttpStatus.CONFLICT);
				}else {
					TeacherSocialMedia tsmAux= _socialMediaService.findSocialMediaByIdAndNickname(teacherSocialMedia.getSocialMedia().getIdSocialMedia(), teacherSocialMedia.getNickname());
					if(tsmAux != null) {
						return new ResponseEntity(new CustomErrorType("the social_media "+ teacherSocialMedia.getSocialMedia().getIdSocialMedia() + " Nickname: "+ teacherSocialMedia.getNickname()+ "Already Exists"),HttpStatus.CONFLICT);
					}
					SocialMedia socialMedia = _socialMediaService.FindById(teacherSocialMedia.getSocialMedia().getIdSocialMedia());
					if(socialMedia==null) {
						return new ResponseEntity(new CustomErrorType("The id_social_media: "+teacherSocialMedia.getSocialMedia().getIdSocialMedia()+" was not found"),HttpStatus.CONFLICT);
					}
					
					teacherSocialMedia.setIdSocialMedia(socialMedia);
					teacherSocialMedia.setTeacher(teacherSaved);
					
					if(tsmAux == null) {
						teacherSaved.getTeachersocialmedias().add(teacherSocialMedia);
					}else{
						LinkedList<TeacherSocialMedia> teacherSocialMedias = new LinkedList();
						teacherSocialMedias.addAll(teacherSaved.getTeachersocialmedias());
						for(int j = 0; j<teacherSocialMedias.size();j++) {
							TeacherSocialMedia teacherSocialMedia2 = teacherSocialMedias.get(j);
							if(teacherSocialMedia.getTeacher().getIdTeacher() == teacherSocialMedia2.getTeacher().getIdTeacher() && teacherSocialMedia.getSocialMedia().getIdSocialMedia() == teacherSocialMedia2.getSocialMedia().getIdSocialMedia()) {
								teacherSocialMedia2.setNickname(teacherSocialMedia.getNickname());
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}else {
								teacherSocialMedias.set(j, teacherSocialMedia2);
							}
						}
						
						teacherSaved.getTeachersocialmedias().clear();
						teacherSaved.getTeachersocialmedias().addAll(teacherSocialMedias);
					}
				}
				
				
			}
		}
		
		_teacherService.updateTeacher(teacherSaved);
		
		return new ResponseEntity<Teacher>(teacherSaved,HttpStatus.OK);
	}
	
}
