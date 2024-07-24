package com.example.main.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class Sessions {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int classId;
    private int classDuration;
    private LocalDate classDate;
    private String classStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int sessionNumber;
    private String meetingLink;

    @ManyToOne
    @JsonBackReference(value = "subcourse-sessions")
    private SubCourse subCourse;

    @ManyToOne
    @JoinColumn(name = "team_name")
    @JsonBackReference(value = "team-sessions")
    private Team team;
}
