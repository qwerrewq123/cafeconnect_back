package com.kong.cc.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageFile {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fileNum;
	
	private String fileContentType;
	private String fileDirectory;
	private String fileName;
	private Long fileSize;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fileUploadDate;
	
	@OneToOne(mappedBy="menuImageFile", fetch=FetchType.LAZY)
	private Menu menu;
	
	@OneToOne(mappedBy="itemImageFile", fetch=FetchType.LAZY)
	private Item item;
}