package com.example.main.entity;



import java.time.LocalDate;
import java.util.List;

import com.example.main.dto.SessionsDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSessionsRequest {
	 private List<LocalDate> dates; // List of dates for sessions
	    private SessionsDTO sessionDTO; // Session details
}