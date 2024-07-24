package com.example.main.service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Task;
import com.example.main.entity.Team;

public interface EmployeeService {
	
	public Employee getEmployeeDetails(String employeeId) throws Exception;

	public Employee updateEmployeeDetails(String employeeId, Employee employee) throws Exception;

	public Employee resetPassword(String employeeId, String currentPassword, String newPassword) throws Exception;
	
	public Set<Course> getCoursesByEmployeeId(String employeeId) throws Exception;
	
	public Course getCourseByCourseName(String courseName);
	
	List<Task> assignTasksToTeam(List<Task> tasks, String teamName);
	
	public List<Task> getTasksByEmployeeId(String employeeId) throws Exception;
	
	public Task updateTaskStatus(String taskId,String status) throws Exception;
	
//	public Course updateCourseProgress(String courseName, int progress);
  
	public String getMeetingLinkByTeamName(String teamName) throws Exception;
	
	public SubCourse getSubCourseBySubName(String subCourseName);
	
	public void markSessionAsAttended(int classId);	
	
	public Team getTeamByEmployeeIdd (String employeeId) throws Exception;
	
	public void uploadTaskFile(String taskId, MultipartFile file);
	
	public Path getTaskFile(String taskId);
		
}

