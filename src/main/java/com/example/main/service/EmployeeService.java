package com.example.main.service;

import java.util.List;
import java.util.Set;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Task;

public interface EmployeeService {
	
	public Employee getEmployeeDetails(String employeeId) throws Exception;

	public Employee updateEmployeeDetails(String employeeId, Employee employee) throws Exception;

	public Employee resetPassword(String employeeId, String currentPassword, String newPassword) throws Exception;
	
	public Set<Course> getCoursesByEmployeeId(String employeeId) throws Exception;
	
	public Course getCourseByCourseName(String courseName);
	
	List<Task> assignTasksToTeam(List<Task> tasks, String teamName);
	
	public List<Task> getTasksByEmployeeId(String employeeId) throws Exception;
	
	public Task updateTaskStatus(String taskId,String status) throws Exception;
	
	public Course updateCourseProgress(String courseName, int progress);

}

