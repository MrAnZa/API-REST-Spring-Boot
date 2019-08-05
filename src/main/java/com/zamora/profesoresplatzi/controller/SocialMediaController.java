package com.zamora.profesoresplatzi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
}
