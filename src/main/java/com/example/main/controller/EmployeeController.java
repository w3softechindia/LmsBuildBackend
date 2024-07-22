package com.example.main.controller;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Task;
import com.example.main.entity.Team;
import com.example.main.service.EmployeeService;
import java.nio.file.Path;


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

	@PreAuthorize("hasAnyRole('Developer', 'Tester','Admin')")
	@GetMapping("/getCourseByCourseName/{courseName}")
	public ResponseEntity<Course> getCourseByCourseName(@PathVariable String courseName) {
		Course courseByCourseName = employeeService.getCourseByCourseName(courseName);
		return new ResponseEntity<Course>(courseByCourseName, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('TeamLead')")
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

//	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	@PutMapping("/updateCourseProgress/{courseName}/{progress}")
//	public ResponseEntity<Course> updateCourseProgress(@PathVariable String courseName, @PathVariable int progress) {
//		Course updateCourseProgress = employeeService.updateCourseProgress(courseName, progress);
//		return new ResponseEntity<Course>(updateCourseProgress, HttpStatus.OK);
//	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getMeetingLinkByTeamName/{teamName}")
	public ResponseEntity<String> getMeetingLinkByTeamName(@PathVariable String teamName) throws Exception {
		String meetingLinkByTeamName = employeeService.getMeetingLinkByTeamName(teamName);
		return new ResponseEntity<String>(meetingLinkByTeamName, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getSubCourseBySubName/{subCourseName}")
	public ResponseEntity<SubCourse> getSubCourseBySubName(@PathVariable String subCourseName) {
		SubCourse courseByCourseName = employeeService.getSubCourseBySubName(subCourseName);
		return new ResponseEntity<SubCourse>(courseByCourseName, HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@PostMapping("/markSessionAsAttended/{classId}")
    public ResponseEntity<String> markSessionAsAttended(@PathVariable int classId) {
        employeeService.markSessionAsAttended(classId);
        return ResponseEntity.ok("Session marked as attended");
    }


	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getTeamByEmployeeId/{employeeId}")
	public ResponseEntity<Team> getTeamByEmployeeIdd(@PathVariable String employeeId) throws Exception {
		Team team = employeeService.getTeamByEmployeeIdd(employeeId);
		return ResponseEntity.ok(team);

	}
	
	  @PreAuthorize("hasAnyRole('Developer', 'Tester')")
	    @PostMapping("/uploadTaskFile/{taskId}")
	    public String uploadTaskFile(@PathVariable("taskId") String taskId, @RequestBody MultipartFile file) {
	        employeeService.uploadTaskFile(taskId, file);
	        return "File uploaded successfully";
	    }
	  
//	  @PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	  @GetMapping("/getTaskFile/{taskId}")
//	    public ResponseEntity<Resource> getTaskFile(@PathVariable String taskId) {
//	        try {
//	            Path filePath = employeeService.getTaskFile(taskId);
//	            Resource resource = new UrlResource(filePath.toUri());
//	            if (resource.exists() || resource.isReadable()) {
//	                return ResponseEntity.ok()
//	                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//	                        .body(resource);
//	            } else {
//	                throw new RuntimeException("Could not read the file!");
//	            }
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	        }
//	    

	  @PreAuthorize("hasAnyRole('Developer', 'Tester')")
	  @GetMapping("/getTaskFile/{taskId}")
	  public ResponseEntity<Resource> getTaskFile(@PathVariable String taskId) {
	      try {
	          Path filePath = employeeService.getTaskFile(taskId);
	          Resource resource = new UrlResource(filePath.toUri());
	          if (resource.exists() || resource.isReadable()) {
	              return ResponseEntity.ok()
	                      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                      .body(resource);
	          } else {
	              throw new RuntimeException("Could not read the file!");
	          }
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	      }
	  }

}

//	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	@PostMapping("/uploadTaskFile/{taskId}")
//	public String uploadTaskFile(@PathVariable("taskId") String taskId, @RequestParam("file") MultipartFile file) {
//	    employeeService.uploadTaskFile(taskId, file);
//	    return "File uploaded successfully";
//	}
	
//	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	@PutMapping("/updateSubCourseProgress/{subCourseName}/{progress}")
//	public ResponseEntity<SubCourse> updateSubCourseProgress(@PathVariable String subCourseName,
//			@PathVariable int progress) {
//		SubCourse updatedSubCourse = employeeService.updateSubCourseProgress(subCourseName, progress);
//		return ResponseEntity.ok(updatedSubCourse);
//	}
//
//	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	@PutMapping("/updateSubCourseStatus/{SubCourseName}/{status}")
//	public ResponseEntity<SubCourse> updateSubCourseStatus(@PathVariable String SubCourseName,
//			@PathVariable String status) throws Exception {
//
//		SubCourse updateTaskStatus = employeeService.updateSubCourseStatus(SubCourseName, status);
//		return new ResponseEntity<SubCourse>(updateTaskStatus, HttpStatus.OK);
//	}

//	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
//	@PostMapping("/createSubCourse")
//	public ResponseEntity<SubCourse>createSubCourse(@RequestBody SubCourse subCourse){
//		
//		SubCourse Course1 = employeeService.createSubCourse(subCourse);
//		return new ResponseEntity<SubCourse>(Course1,HttpStatus.OK);
//	}