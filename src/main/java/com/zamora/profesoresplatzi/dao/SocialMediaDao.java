package com.zamora.profesoresplatzi.dao;

import java.util.List;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;

public interface SocialMediaDao {

	void saveSocialMedia(SocialMedia socialmedia);
	
	void deleteSoialMedia(Long idSoialMedia);
	
	void updateSocialMedia(SocialMedia socialmedia);
	
	List<SocialMedia> findAllSocialMedias();
	
	SocialMedia FindById(Long idSocialMedia);
	
	SocialMedia FindByName(String name);
	
	TeacherSocialMedia findSocialMediaByIdAndName(Long IdSocialMedia, String nickname);
}
