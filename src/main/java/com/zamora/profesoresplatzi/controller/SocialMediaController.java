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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.Teacher;
import com.zamora.profesoresplatzi.service.SocialMediaService;
import com.zamora.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	
		@Autowired
		SocialMediaService _socialMediaService;
	//GET
	@RequestMapping(value="/socialMedias",method = RequestMethod.GET, headers ="Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedia(@RequestParam(value="name", required=false) String name){
		
		List<SocialMedia> socialMedias= new ArrayList<>();
		
		if(name == null) {
			socialMedias = _socialMediaService.findAllSocialMedias();
			if(socialMedias.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT); 
			}else{
				return new ResponseEntity<List<SocialMedia>>(socialMedias,HttpStatus.OK);
			}
		}else {
			SocialMedia socialMedia = _socialMediaService.FindByName(name);
			if(socialMedia == null) {
				return new ResponseEntity(HttpStatus.NOT_FOUND); 
			}
			socialMedias.add(socialMedia);
			return new ResponseEntity<List<SocialMedia>>(socialMedias,HttpStatus.OK);
		}
		
	}
	//GET
	@RequestMapping(value= "/socialMedias/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<SocialMedia>	getSocialMediaById(@PathVariable("id") Long idSocialMedia){
		
		if(idSocialMedia == null || idSocialMedia <=0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"),HttpStatus.CONFLICT); 
		}
		
		SocialMedia socialMedia = _socialMediaService.FindById(idSocialMedia);
		
		if(socialMedia ==null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<SocialMedia>(socialMedia,HttpStatus.OK);
			
	}
	//POST	
	@RequestMapping(value= "/socialMedias", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia, UriComponentsBuilder uriComponentBuilder){
		if(socialMedia.getName().equals(null) || socialMedia.getName().isEmpty()) {
			
			return new ResponseEntity(new CustomErrorType("SocialMedia name is required"),HttpStatus.CONFLICT);
		}
		if(_socialMediaService.FindByName(socialMedia.getName())!= null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		_socialMediaService.saveSocialMedia(socialMedia);
		SocialMedia socialMedia2 = _socialMediaService.FindByName(socialMedia.getName());
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uriComponentBuilder.path("/v1/socialMedias/{id}").buildAndExpand(socialMedia2.getIdSocialMedia()).toUri());
		
		return new ResponseEntity<String>(headers,HttpStatus.CREATED);
	}
	
	//PATCH
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.PATCH, headers = "Accept=application/json")
	public ResponseEntity<?> updateSocialMeida(@PathVariable("id") Long idSocialMedia, @RequestBody SocialMedia socialMedia){
		
		SocialMedia currentSocialMedia = _socialMediaService.FindById(idSocialMedia);
		
		if(idSocialMedia == null || idSocialMedia <=0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"),HttpStatus.CONFLICT);
		}
		
		if(currentSocialMedia == null) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		currentSocialMedia.setName(socialMedia.getName());
		currentSocialMedia.setIcon(socialMedia.getIcon());
		_socialMediaService.updateSocialMedia(currentSocialMedia);
		
		return new ResponseEntity<SocialMedia>(currentSocialMedia,HttpStatus.OK);
	}
	//DELETE
	@RequestMapping(value="/socialMedias/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public ResponseEntity<?> deleteSocialMedia(@PathVariable("id") Long idSocialMedia){
		
		if(idSocialMedia == null || idSocialMedia <=0) {
			return new ResponseEntity(new CustomErrorType("idSocialMedia is required"),HttpStatus.CONFLICT);
		}
		SocialMedia socialMedia = _socialMediaService.FindById(idSocialMedia);
		
		if(socialMedia  == null){
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}		
		_socialMediaService.deleteSoialMedia(idSocialMedia);
		return new ResponseEntity<SocialMedia>(HttpStatus.OK);
	}
	
	public static final String SOCIALMEDIA_UPLOADED_FOLDER="images/socialmedias/";
	
	//Upload ICON
	@RequestMapping(value="socialmedias/image", method = RequestMethod.POST, headers=("content-type=multipart/form-data"))
	public ResponseEntity<byte[]> uploadSocialMediaImage(@RequestParam("id_socialMedia") Long idSocialMedia, @RequestParam("file") MultipartFile multipartfile, UriComponentsBuilder uriComponentBuilder){
		
		if(idSocialMedia == null) {
			return new ResponseEntity(new CustomErrorType("Please set id_Teacher"),HttpStatus.CONFLICT);
		}
		if(multipartfile.isEmpty()) {
			return new ResponseEntity(new CustomErrorType("Please seselect a file to upload"),HttpStatus.CONFLICT);
		}
		
		SocialMedia socialMedia = _socialMediaService.FindById(idSocialMedia);
		
		if(socialMedia == null) {
			return new ResponseEntity(new CustomErrorType("SocialMedia With id: "+ idSocialMedia + " Not Found"),HttpStatus.NOT_FOUND);
		}
		
		if(!socialMedia.getIcon().isEmpty() || socialMedia.getIcon() != null) {
			String filename = socialMedia.getIcon();
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
					
			String fileName = String.valueOf(socialMedia.getIdSocialMedia()) + "-pictureSocialMedia-"+dateName+"." + multipartfile.getContentType().split("/")[1];
			socialMedia.setIcon(SOCIALMEDIA_UPLOADED_FOLDER+fileName);
			
			byte[] bytes = multipartfile.getBytes();
			 Path path = Paths.get(SOCIALMEDIA_UPLOADED_FOLDER+fileName);
			 Files.write(path, bytes);
			 
			_socialMediaService.updateSocialMedia(socialMedia);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
			 
 		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error During Upload"),HttpStatus.NOT_FOUND);
		}
		
	}
	
	//GET Icon
	@RequestMapping(value="/socialmedias/{id_socialMedia}/images",method=RequestMethod.GET)
	public ResponseEntity<byte[]> getSocialMediaImage(@PathVariable("id_socialMedia") Long idSocialMedia){
		
		if(idSocialMedia==null) {
		return new ResponseEntity(new CustomErrorType("Please set id_socialMedia"),HttpStatus.CONFLICT);
		}
		
		SocialMedia socialMedia = _socialMediaService.FindById(idSocialMedia);
		
		if(socialMedia==null) {
			return new ResponseEntity(new CustomErrorType("SocialMedia with id: "+idSocialMedia+ " Not Found"),HttpStatus.NOT_FOUND);
		}
		try {
			String filename = socialMedia.getIcon();
			Path path = Paths.get(filename);
			File f = path.toFile();
			if(!f.exists()) {
				return new ResponseEntity(new CustomErrorType("Image Not Found"),HttpStatus.NOT_FOUND);
			}
			
			byte[] image = Files.readAllBytes(path);
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
			
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity(new CustomErrorType("Error to show image"),HttpStatus.CONFLICT);
		}
	}
	//DELETE Icon
		@RequestMapping(value="/socialmedias/{id_socialMedia}/images",method=RequestMethod.DELETE, headers = "Accept=application/json")
		public ResponseEntity<?> deleteSocialMediaImage(@PathVariable("id_socialMedia") Long idSocialMedia){
		
			if(idSocialMedia==null) {
				return new ResponseEntity(new CustomErrorType("Please set id_socialMedia"),HttpStatus.CONFLICT);
				}
				
				SocialMedia socialMedia = _socialMediaService.FindById(idSocialMedia);
				
				if(socialMedia==null) {
					return new ResponseEntity(new CustomErrorType("SocialMedia with id: "+idSocialMedia+ " Not Found"),HttpStatus.NOT_FOUND);
				}
				
				if(socialMedia.getIcon().isEmpty() || socialMedia.getIcon() == null) {
					return new ResponseEntity(new CustomErrorType("This SocialMedia doesn`t have image assigned"),HttpStatus.NOT_FOUND);
				}
				
				String fileName = socialMedia.getIcon();
				Path path = Paths.get(fileName);
				File file=path.toFile();
				if(file.exists()) {
					file.delete();
				}
				
				socialMedia.setIcon("");
				_socialMediaService.updateSocialMedia(socialMedia);
				return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
		}
}
