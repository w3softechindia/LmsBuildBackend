package com.example.main.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.dto.CourseDto;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Team;
import com.example.main.exception.InvalidEmployeeId;
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

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getAllTeams/{employeeId}")
	public ResponseEntity<List<Team>> getAllTeams(@PathVariable String employeeId) throws Exception {
		List<Team> teams = teamLeadService.getAllTeams(employeeId);
		return ResponseEntity.ok(teams);

	}

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getTeamByName/{teamName}")
	public ResponseEntity<Team> getTeamByName(@PathVariable String teamName) throws Exception {
		Team teamByName = teamLeadService.getTeamByName(teamName);
		return ResponseEntity.ok(teamByName);

	}

	@PreAuthorize("hasRole('TeamLead')")
	@DeleteMapping("/deleteEmployeeFromTeam/{employeeId}")
	public ResponseEntity<String> deleteEmployeeFromTeam(@PathVariable String employeeId) throws Exception {
		String deleteEmployeeFromTeam = teamLeadService.deleteEmployeeFromTeam(employeeId);
		return ResponseEntity.ok(deleteEmployeeFromTeam);
	}


	@PreAuthorize("hasRole('TeamLead')")
	@PutMapping("/updateTeam/{teamName}")
	public ResponseEntity<Team> updateTeam(@PathVariable String teamName, @RequestBody Team updatedTeam) throws Exception {
		Team updateTeam = teamLeadService.updateTeam(teamName, updatedTeam);
		return ResponseEntity.ok(updateTeam);
	}

	@PreAuthorize("hasRole('TeamLead')")
	@DeleteMapping("/deleteTeam/{teamName}")
	public ResponseEntity<String> deleteTeam(@PathVariable String teamName) throws Exception {
		String deleteTeam = teamLeadService.deleteTeam(teamName);
		return ResponseEntity.ok(deleteTeam);
	}
	
	@PreAuthorize("hasRole('TeamLead')")
	@PostMapping("/{employeeId}/uploadProfilePhoto")
	public ResponseEntity<byte[]> uploadProfilePhoto(@PathVariable String employeeId, @RequestParam("file") MultipartFile file) throws Exception {
	    System.out.println("Received upload request for employeeId: " + employeeId);
	    System.out.println("File name: " + file.getOriginalFilename());
	    try {
	        byte[] fileContent = teamLeadService.uploadProfilePhoto(employeeId, file);
	        return ResponseEntity.ok().body(fileContent);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(null);
	    }
	}


}

