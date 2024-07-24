package com.example.main.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionsDTO {
    private int classId;
    private int classDuration;
    private LocalDate classDate;
    private String classStatus;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int sessionNumber;
    private String meetingLink;  // Include meeting link here
    private String timeStatus;
}
