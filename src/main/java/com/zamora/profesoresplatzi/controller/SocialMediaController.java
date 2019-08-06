package com.zamora.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.service.SocialMediaService;

@Controller
@RequestMapping("/v1")
public class SocialMediaController {
	
		@Autowired
		SocialMediaService _socialMediaService;
	
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
	
	@RequestMapping(value= "/socialMedias", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createSocialMedia(@RequestBody SocialMedia socialMedia, UriComponentsBuilder uriComponentBuilder){
		if(socialMedia.getName().equals(null) || socialMedia.getName().isEmpty()) {
			
			return new ResponseEntity(HttpStatus.NO_CONTENT);
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
	
}
