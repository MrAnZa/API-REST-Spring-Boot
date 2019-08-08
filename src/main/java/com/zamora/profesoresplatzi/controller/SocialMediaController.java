package com.zamora.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.service.SocialMediaService;
import com.zamora.profesoresplatzi.util.CustomErrorType;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	
		@Autowired
		SocialMediaService _socialMediaService;
	//GET
	@RequestMapping(value="/socialMedias",method = RequestMethod.GET, headers ="Accept=application/json")
	public ResponseEntity<List<SocialMedia>> getSocialMedia(){
		
		List<SocialMedia> socialMedias= new ArrayList<>();
		socialMedias = _socialMediaService.findAllSocialMedias();
		if(socialMedias.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT); 
		}else{
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
	
	
	
}
