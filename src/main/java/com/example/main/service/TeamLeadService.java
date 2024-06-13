package com.example.main.service;

import java.util.List;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Team;

public interface TeamLeadService {

	public Employee getTeamLead(String employeeId) throws Exception;
	
	public List<Employee> getAllEmployees();
	
	public Team addTeamToEmployee(Team team, String employeeId) throws Exception;

	public Course addCourse(Course course, List<SubCourse> subCourse) throws Exception;
	
	public List<Course> getAllCourses() throws Exception;

	

}
