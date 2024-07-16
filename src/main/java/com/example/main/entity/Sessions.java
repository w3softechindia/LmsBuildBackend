package com.example.main.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Sessions {
	
	@Id
	@GeneratedValue (strategy = (GenerationType.IDENTITY))
	private int classId;
	private int classDuration;
	private LocalDate classDate;
	private String classStatus;
	
	@ManyToOne
	@JsonBackReference
	private SubCourse subCourse;
	
}
