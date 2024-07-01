package com.example.main.controller;

import java.util.List;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Task;
import com.example.main.repository.CourseRepository;
import com.example.main.service.EmployeeService;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;

	@PreAuthorize("hasAnyRole('Developer', 'Tester', 'TeamLead')")
	@GetMapping("/getEmployeeDetails/{employeeId}")
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable String employeeId) throws Exception {

		Employee employeeDetails = employeeService.getEmployeeDetails(employeeId);
		return new ResponseEntity<Employee>(employeeDetails, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester', 'TeamLead')")
	@PutMapping("/updateEmployeeDetails/{employeeId}")
	public ResponseEntity<Employee> updateEmployeeDetails(@PathVariable String employeeId,
			@RequestBody Employee employee) throws Exception {
		Employee updateEmployeeDetails = employeeService.updateEmployeeDetails(employeeId, employee);
		return new ResponseEntity<Employee>(updateEmployeeDetails, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester','TeamLead','Admin')")
	@PutMapping("/resetPassword/{employeeId}/{currentPassword}/{newPassword}")
	public ResponseEntity<Employee> resetPassword(@PathVariable String employeeId, @PathVariable String currentPassword,
			@PathVariable String newPassword) throws Exception {

		Employee resetPassword = employeeService.resetPassword(employeeId, currentPassword, newPassword);
		return new ResponseEntity<Employee>(resetPassword, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getCoursesByEmployeeId/{employeeId}")
	public ResponseEntity<Set<Course>> getCoursesByEmployeeId(@PathVariable String employeeId) {
		try {
			Set<Course> courses = employeeService.getCoursesByEmployeeId(employeeId);
			return ResponseEntity.ok(courses);
		} catch (Exception e) {
			return ResponseEntity.status(404).body(null);
		}
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getCourseByCourseName/{courseName}")
	public ResponseEntity<Course> getCourseByCourseName(@PathVariable String courseName) {
		Course courseByCourseName = employeeService.getCourseByCourseName(courseName);
		return new ResponseEntity<Course>(courseByCourseName, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PostMapping("/assignTasksToTeam/{teamName}")
	public ResponseEntity<List<Task>> assignTasksToTeam(@RequestBody List<Task> tasks, @PathVariable String teamName) {
		List<Task> assignTasksToTeam = employeeService.assignTasksToTeam(tasks, teamName);
		return ResponseEntity.ok(assignTasksToTeam);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getTasksByEmployeeId/{employeeId}")
	public ResponseEntity<List<Task>> getTasksByEmployeeId(@PathVariable String employeeId) throws Exception {

		List<Task> tasksByEmployeeId = employeeService.getTasksByEmployeeId(employeeId);
		return ResponseEntity.ok(tasksByEmployeeId);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PutMapping("/updateTaskStatus/{taskId}/{status}")
	public ResponseEntity<Task> updateTaskStatus(@PathVariable String taskId, @PathVariable String status)
			throws Exception {

		Task updateTaskStatus = employeeService.updateTaskStatus(taskId, status);
		return new ResponseEntity<Task>(updateTaskStatus, HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PutMapping("/updateCourseProgress/{courseName}/{progress}")
	public ResponseEntity<Course> updateCourseProgress(@PathVariable String courseName, @PathVariable int progress) {
		Course updateCourseProgress = employeeService.updateCourseProgress(courseName, progress);
		return new ResponseEntity<Course>(updateCourseProgress, HttpStatus.OK);
	}
}
