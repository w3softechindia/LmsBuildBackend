package com.example.main.dto;

import java.util.List;

import com.example.main.entity.SubCourse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
	private String courseName;
	private int courseDuration;
	private List<SubCourse> subCourses;

}
