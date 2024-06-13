package com.example.main.controller;

import java.util.List;
import java.util.stream.Collectors;


import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.main.dto.CourseDto;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Team;
import com.example.main.service.TeamLeadService;

@RestController
public class TeamLeadController {

	@Autowired
	private TeamLeadService teamLeadService;

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getTeamLead/{employeeId}")
	public ResponseEntity<Employee> getTeamLead(@PathVariable String employeeId) throws Exception {

		Employee employee = teamLeadService.getTeamLead(employeeId);
		return ResponseEntity.ok(employee);

	}

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> emp = teamLeadService.getAllEmployees();
		return ResponseEntity.ok(emp);
	}

	@PreAuthorize("hasRole('TeamLead')")
	@PostMapping("/addCourse")
	public ResponseEntity<Course> addCourse(@RequestBody CourseDto courseDTO) {
		try {
			Course course = new Course();
			course.setCourseName(courseDTO.getCourseName());
			course.setCourseDuration(courseDTO.getCourseDuration());

			List<SubCourse> subCourses = courseDTO.getSubCourses().stream().map(subCourseDTO -> {
				SubCourse subCourse = new SubCourse();
				subCourse.setSubCourseName(subCourseDTO.getSubCourseName());
				subCourse.setSubCourseDuration(subCourseDTO.getSubCourseDuration());
				return subCourse;
			}).collect(Collectors.toList());

			Course createdCourse = teamLeadService.addCourse(course, subCourses);
			return ResponseEntity.ok(createdCourse);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PreAuthorize("hasRole('TeamLead')")
	@PostMapping("/addTeamToEmployee/{employeeId}")
	public ResponseEntity<Team> addTeamToEmployee(@RequestBody Team team, @PathVariable String employeeId)
			throws Exception {
		Team employee = teamLeadService.addTeamToEmployee(team, employeeId);
		return ResponseEntity.ok(employee);

	}

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getAllCourses")
	public ResponseEntity<List<Course>> getAllCourses() throws Exception {
		List<Course> list = teamLeadService.getAllCourses();
		return ResponseEntity.ok(list);
	}

}
