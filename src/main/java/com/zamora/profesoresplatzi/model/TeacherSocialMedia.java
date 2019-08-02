package com.zamora.profesoresplatzi.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="teacher_social_media")
public class TeacherSocialMedia implements Serializable{

	@Id
	@Column(name="id_teacher_social_media")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idTeacherSocialMedia;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_teacher")
	private Teacher idTeacher;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_social_media")	
	private SocialMedia idSocialMedia;
	
	
	@Column(name="nickname")
	private String nickname;
	
	public TeacherSocialMedia() {
		
	}
	

	public TeacherSocialMedia(Teacher idTeacher, SocialMedia idSocialMedia, String nickname) {
		this.idTeacher = idTeacher;
		this.idSocialMedia = idSocialMedia;
		this.nickname = nickname;
	}


	public Long getIdTeacherSocialMedia() {
		return idTeacherSocialMedia;
	}
	public void setIdTeacherSocialMedia(Long idTeacherSocialMedia) {
		this.idTeacherSocialMedia = idTeacherSocialMedia;
	}
	public Teacher getIdTeacher() {
		return idTeacher;
	}
	public void setIdTeacher(Teacher idTeacher) {
		this.idTeacher = idTeacher;
	}
	public SocialMedia getIdSocialMedia() {
		return idSocialMedia;
	}
	public void setIdSocialMedia(SocialMedia idSocialMedia) {
		this.idSocialMedia = idSocialMedia;
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
