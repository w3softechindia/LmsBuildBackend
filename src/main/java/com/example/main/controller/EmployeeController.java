package com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.main.entity.Course;

import com.example.main.entity.Employee;
import com.example.main.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/getEmployeeDetails/{employeeId}")
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable String employeeId) throws Exception {

		Employee employeeDetails = employeeService.getEmployeeDetails(employeeId);
		return new ResponseEntity<Employee>(employeeDetails, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PutMapping("/updateEmployeeDetails/{employeeId}")
	public ResponseEntity<Employee> updateEmployeeDetails(@PathVariable String employeeId,
			@RequestBody Employee employee) throws Exception {

		Employee updateEmployeeDetails = employeeService.updateEmployeeDetails(employeeId, employee);
		return new ResponseEntity<Employee>(updateEmployeeDetails, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PutMapping("/resetPassword/{employeeId}/{currentPassword}/{newPassword}")
	public ResponseEntity<Employee> resetPassword(@PathVariable String employeeId,@PathVariable String currentPassword,
		@PathVariable String newPassword) throws Exception {

		Employee resetPassword = employeeService.resetPassword(employeeId, currentPassword, newPassword);
		return new ResponseEntity<Employee>(resetPassword, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getCourse/{courseName}")
	public ResponseEntity<Course> getCourseByName(@PathVariable String courseName) throws Exception {
		try {
			Course course = employeeService.getCourseByName(courseName);
			return ResponseEntity.ok(course);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}

