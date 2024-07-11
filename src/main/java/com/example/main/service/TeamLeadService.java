package com.example.main.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Team;

public interface TeamLeadService {

	public Employee getTeamLead(String employeeId) throws Exception;
	
	public List<Employee> getAllEmployees();
	
	public Team addTeamToEmployee(Team team, String employeeId) throws Exception;

	public Course addCourse(Course course, List<SubCourse> subCourse) throws Exception;
	
	public List<Team> getAllTeams(String employeeId) throws Exception;
	
	public Set<Course> getCourses(String employeeId) throws Exception;
	
	public List<Course> getAllCourses() throws Exception;
	
	public Team getTeamByName (String teamName) throws Exception;
	
	public String deleteEmployeeFromTeam(String employeeId) throws Exception;

	public Team updateTeam(String teamName,Team updatedTeam) throws Exception;
	
	public String deleteTeam(String teamName) throws Exception;
  
	public void uploadPhoto(String employeeId, MultipartFile file) throws IOException, Exception;
	
	byte[] getProfilePicture(String employeeId) throws IOException;
	
	public void updatePhoto(String employeeId, MultipartFile photo) throws IOException, Exception;
	
	public long getTotalTeamsByTeamLead(String employeeId);
	
//	public long getTotalCoursesByTeamLead(String employeeId) throws Exception;
}
