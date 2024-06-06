package com.example.main.service;

import java.util.List;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Team;

public interface TeamLeadService {

	public Employee getTeamLead(String employeeId) throws Exception;
	
	public List<Employee> getAllEmployees();
	
	public Course addCourse(Course course) throws Exception;
	
	public Team addTeamToEmployee(Team team, String employeeId) throws Exception;
	

	
}
