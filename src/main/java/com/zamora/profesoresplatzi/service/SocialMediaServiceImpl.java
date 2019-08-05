package com.zamora.profesoresplatzi.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zamora.profesoresplatzi.dao.SocialMediaDao;
import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;

@Service("socialMediaService")
@Transactional
public class SocialMediaServiceImpl implements SocialMediaService {

	@Autowired
	private SocialMediaDao _socialMediaDao;
	
	@Override
	public void saveSocialMedia(SocialMedia socialmedia) {
		// TODO Auto-generated method stub
		_socialMediaDao.saveSocialMedia(socialmedia);
		
	}

	@Override
	public void deleteSoialMedia(Long idSoialMedia) {
		// TODO Auto-generated method stub
		_socialMediaDao.deleteSoialMedia(idSoialMedia);
		
	}

	@Override
	public void updateSocialMedia(SocialMedia socialmedia) {
		// TODO Auto-generated method stub
		_socialMediaDao.updateSocialMedia(socialmedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		// TODO Auto-generated method stub
		return _socialMediaDao.findAllSocialMedias();
	}

	@Override
	public SocialMedia FindById(Long idSocialMedia) {
		// TODO Auto-generated method stub00
		return _socialMediaDao.FindById(idSocialMedia);
	}

	@Override
	public SocialMedia FindByName(String name) {
		// TODO Auto-generated method stub
		return _socialMediaDao.FindByName(name);
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long IdSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		return _socialMediaDao.findSocialMediaByIdAndName(IdSocialMedia, nickname);
	}

}
