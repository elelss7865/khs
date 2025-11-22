package com.seazon.board.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class TravelForm {

	@NotEmpty(message="제목은 필수항목입니다.")
	@Size(max=200)
	private String subject;
	
	private MultipartFile file;
	
	@NotEmpty(message="내용은 필수항목입니다.")
	// 축제 소개
	private String travelIntro;     
	
	// 카테고리
    private String category;     
	
 // 축제 일정
    private String travelInfo_day;     

 // 축제 장소 
	private String travelInfo_place;     

	// 축제 비용
	private String travelInfo_pay;     
	
	// 축제 관련 전화번호
	private String travelInfo_phone;    
	
	// 축제 주체자
	private String travelInfo_organizer;     
	
	// 축제 공식홈페이지
	private String travelInfo_homepage;         
	
	 // 축제 내용
    private String content;        
	
 // 축제 이미지
	private MultipartFile contentFile;      
	
}
