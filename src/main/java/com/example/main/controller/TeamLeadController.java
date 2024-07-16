package com.example.main.controller;

import java.io.IOException;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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

//	@PreAuthorize("hasRole('TeamLead')")
//	 @GetMapping("/getCoursesByEmployeeId/{employeeId}")
//    public ResponseEntity<Set<Course>> getCoursesByEmployeeId(@PathVariable String employeeId) {
//        try {
//            Set<Course> courses = teamLeadService.getCoursesByEmployeeId(employeeId);
//            return ResponseEntity.ok(courses);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

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

	@PreAuthorize("hasAnyRole('TeamLead','Admin')")
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
	public ResponseEntity<Team> updateTeam(@PathVariable String teamName, @RequestBody Team updatedTeam)
			throws Exception {
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
	@GetMapping("/getCourses/{employeeId}")
	public ResponseEntity<Set<Course>> getCourses(@PathVariable String employeeId) throws Exception {
		Set<Course> course = teamLeadService.getCourses(employeeId);
		return ResponseEntity.ok(course);
	}

	@PreAuthorize("hasAnyRole('ROLE_TeamLead', 'ROLE_Developer', 'ROLE_Tester')")
	@PostMapping("/uploadPhoto/{employeeId}")
	public ResponseEntity<String> uploadPhoto(@PathVariable String employeeId, @RequestParam("file") MultipartFile file)
			throws Exception {
		try {
			teamLeadService.uploadPhoto(employeeId, file);
			return ResponseEntity.ok("Photo uploaded successfully for user with employeeId ");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading photo: " + e.getMessage());
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_TeamLead', 'ROLE_Developer', 'ROLE_Tester')")
	@GetMapping("/getPhoto/{employeeId}")
	public ResponseEntity<ByteArrayResource> getPhoto(@PathVariable String employeeId) {
		try {
			// Get the photo bytes for the given email
			byte[] photoBytes = teamLeadService.getProfilePicture(employeeId);

			// Create a ByteArrayResource from the photo bytes
			ByteArrayResource resource = new ByteArrayResource(photoBytes);
			// Return ResponseEntity with the resource
			return ResponseEntity.ok().header("Content-Type", "image/jpeg").body(resource);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_TeamLead', 'ROLE_Developer', 'ROLE_Tester')")
	@PutMapping("/updatePhoto/{employeeId}")
	public ResponseEntity<?> updatePhoto(@PathVariable String employeeId, @RequestParam("photo") MultipartFile photo)
			throws Exception {
		try {
			teamLeadService.updatePhoto(employeeId, photo);
			return ResponseEntity.ok().build(); // Return 200 OK if photo is updated successfully
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build(); // Return 400 Bad Request if photo is empty or user does not
														// exist
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return 500 Internal Server Error
																					// for other IO exceptions
		}
	}

	@PreAuthorize("hasRole('TeamLead')")
	@GetMapping("/getTotalTeamsByTeamLead/{employeeId}")
	public ResponseEntity<Long> getTotalTeamsByTeamLead(@PathVariable String employeeId) {
		long totalTeams = teamLeadService.getTotalTeamsByTeamLead(employeeId);
		return ResponseEntity.ok(totalTeams);
	}

//	@PreAuthorize("hasRole('TeamLead')")
//	@GetMapping("/getTasksByTeamLead/{employeeId}")
//	public ResponseEntity<List<Task>> getTasksByTeamLead(@PathVariable String employeeId) throws Exception {
//		List<Task> teamlead = teamLeadService.getTasksByTeamlead(employeeId);
//		return ResponseEntity.ok(teamlead);
//	}
}
