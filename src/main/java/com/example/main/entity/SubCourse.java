package com.example.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCourse {

	@Id
	private String subCourseName;
	private int subCourseDuration;
//	private String meetingLink;
	private int progress;
	private String status;

	@ManyToOne
	@JsonBackReference
	private Course course;
}
