package com.example.main.service;

import java.util.Set;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;

public interface EmployeeService {
	
	public Employee getEmployeeDetails(String employeeId) throws Exception;

	public Employee updateEmployeeDetails(String employeeId, Employee employee) throws Exception;

	public Employee resetPassword(String employeeId, String currentPassword, String newPassword) throws Exception;
	
	public Set<Course> getCoursesByEmployeeId(String employeeId) throws Exception;
	
	public Course getCourseByCourseName(String courseName);
}

