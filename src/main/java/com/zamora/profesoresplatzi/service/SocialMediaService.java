package com.zamora.profesoresplatzi.service;

import java.util.List;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaService {
	
	void saveSocialMedia(SocialMedia socialmedia);
	
	void deleteSoialMedia(Long idSoialMedia);
	
	void updateSocialMedia(SocialMedia socialmedia);
	
	List<SocialMedia> findAllSocialMedias();
	
	SocialMedia FindById(Long idSocialMedia);
	
	SocialMedia FindByName(String name);
	
	TeacherSocialMedia findSocialMediaByIdAndName(Long IdSocialMedia, String nickname);
}
