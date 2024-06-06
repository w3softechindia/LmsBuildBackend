package com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

	@PutMapping("/updateEmployeeDetails/{employeeId}")
	public ResponseEntity<Employee> updateEmployeeDetails(@PathVariable String employeeId,
			@RequestBody Employee employee) throws Exception {

		Employee updateEmployeeDetails = employeeService.updateEmployeeDetails(employeeId, employee);
		return new ResponseEntity<Employee>(updateEmployeeDetails, HttpStatus.OK);
	}
}
