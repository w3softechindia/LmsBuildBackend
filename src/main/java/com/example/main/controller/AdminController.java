package com.example.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.main.entity.Employee;
import com.example.main.service.AdminService;
import com.example.main.service.EmployeeService;

import jakarta.annotation.PostConstruct;

@RestController
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	
	@PostConstruct
	public void initRoleAndAdmin() {
		adminService.initRoleAndAdmin();
	}

	@PostMapping("/addAdmin")
	public ResponseEntity<Employee> addAdmin(@RequestBody Employee admin) {

		Employee addAdmin = adminService.addAdmin(admin);
		return new ResponseEntity<Employee>(addAdmin, HttpStatus.OK);

	}
	

    
	@PreAuthorize("hasRole('Admin')")
	@PostMapping("/addEmployee/{roleName}")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee,@PathVariable String roleName) {

		Employee addEmployee = adminService.addEmployee(employee,roleName);
		return new ResponseEntity<Employee>(addEmployee, HttpStatus.OK);
	}

}
