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
	public TeacherSocialMedia findSocialMediaByIdAndNickname(Long IdSocialMedia, String nickname) {
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

	@Override
	public TeacherSocialMedia findSocialMediaByIdTeacherAndIdSocialMedia(Long idTeacher, Long idSocialMedia) {
		// TODO Auto-generated method stub
		List<Object[]> objs= getSession().createQuery(
				"from TeacherSocialMedia tsm join tsm.socialMedia sm "
				+ "join tsm.teacher t where sm.idSocialMedia = :id_social_media "
				+ "and t.idTeacher = :id_teacher")
				.setParameter("id_social_media", idSocialMedia)
				.setParameter("id_teacher", idTeacher).list();
		
		if(objs.size()>0) {
			for(Object[] objects : objs) {
				for(Object object : objects) {
					if(object instanceof TeacherSocialMedia) {
					return (TeacherSocialMedia) object;	
					}
				}
			}
		}
		return null;
	}

}
