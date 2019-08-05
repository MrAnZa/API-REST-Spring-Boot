package com.zamora.profesoresplatzi.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.zamora.profesoresplatzi.model.SocialMedia;
import com.zamora.profesoresplatzi.model.TeacherSocialMedia;

@Repository
@Transactional
public class SocialMediaDaoImpl extends AbstractSession implements SocialMediaDao {

	@Override
	public void saveSocialMedia(SocialMedia socialmedia) {
		// TODO Auto-generated method stub
		getSession().persist(socialmedia);
		
	}

	@Override
	public void deleteSoialMedia(Long idSocialMedia) {
		// TODO Auto-generated method stub
		SocialMedia socialmedia = FindById(idSocialMedia);
		if(socialmedia!=null) {
			getSession().delete(socialmedia);
		}
	}

	@Override
	public void updateSocialMedia(SocialMedia socialmedia) {
		// TODO Auto-generated method stub
		getSession().update(socialmedia);
	}

	@Override
	public List<SocialMedia> findAllSocialMedias() {
		// TODO Auto-generated method stub
		return getSession().createQuery("from SocialMedia").list();
	}

	@Override
	public SocialMedia FindById(Long idSocialMedia) {
		// TODO Auto-generated method stub
		return (SocialMedia) getSession().get(SocialMedia.class, idSocialMedia);
	}

	@Override
	public SocialMedia FindByName(String name) {
		// TODO Auto-generated method stub
		return  (SocialMedia) getSession().createQuery("from SocialMedia where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public TeacherSocialMedia findSocialMediaByIdAndName(Long IdSocialMedia, String nickname) {
		// TODO Auto-generated method stub
		 List<Object[]> objects = getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "where sm.idSocialMedia = :idSocialMedia and tsm.nickname = :nickname")
				.setParameter("idSocialMedia", IdSocialMedia)
				.setParameter("nickname", nickname).list();
		 if(objects.size()>0) {
			 for (Object[] objects2 : objects) {
				for (Object object : objects2) {
					if(object instanceof TeacherSocialMedia) {
			
						return (TeacherSocialMedia) object;
					}
						
				}
			}
		 }
		 return null;
	}

}
