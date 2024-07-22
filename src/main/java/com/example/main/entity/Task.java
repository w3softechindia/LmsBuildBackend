package com.example.main.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Task {
	
	@Id
    private String taskId;
	private String taskName;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private String fileName;
    
    @ManyToOne
    @JoinColumn(name = "teamName")
    @JsonBackReference
    private Team team;
	
}
