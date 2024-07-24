package com.example.main.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sessions {

	@Id
	@GeneratedValue(strategy = (GenerationType.IDENTITY))
	private int classId;
	private int classDuration;
	private LocalDate classDate;
	private String classStatus;

	private LocalDateTime startTime; // Add start time field
	private LocalDateTime endTime; // Add end time field

	@ManyToOne
	@JsonBackReference
	private SubCourse subCourse;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private Employee employee;
}
