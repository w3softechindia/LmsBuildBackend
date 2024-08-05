package com.example.main.controller;

import java.nio.file.Path;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.dto.SessionsDTO;
import com.example.main.entity.Course;
import com.example.main.entity.CreateSessionsRequest;
import com.example.main.entity.Employee;
import com.example.main.entity.EmployeeMeetingRecord;
import com.example.main.entity.Sessions;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Task;
import com.example.main.entity.Team;
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
	public ResponseEntity<Task>updateTaskStatus(@PathVariable String taskId, @PathVariable String status)
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

	@PreAuthorize("hasAnyRole('Developer', 'Tester','TeamLead')")
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
	    
	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getTaskFile/{taskId}")
	public ResponseEntity<Resource> getTaskFile(@PathVariable String taskId) {
		try {
			Path filePath = employeeService.getTaskFile(taskId);
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester','TeamLead')")
	@GetMapping("/countSessions")
	public long countSessions() {
		return employeeService.countSessions();
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester','TeamLead')")
	@GetMapping("/getAllSessions")
	public List<Sessions> getAllSessions() {
		return employeeService.getAllSessions();
	}

	@PreAuthorize("hasAnyRole('TeamLead')")
	@PostMapping("/createSession")
	public ResponseEntity<Sessions> createSession(@RequestBody Sessions session) {
		Sessions session2 = employeeService.createSession(session);
		return ResponseEntity.ok(session2);
	}

	@PreAuthorize("hasAnyRole('Developer', 'Tester','TeamLead')")
	@GetMapping("/getSessionsByTeamName/{teamName}")
	public ResponseEntity<List<SessionsDTO>> getSessionsByTeamName(@PathVariable String teamName) {
		List<SessionsDTO> sessions = employeeService.getSessionsByTeamName(teamName);
		return ResponseEntity.ok(sessions);
	}

	@PreAuthorize("hasAnyRole('TeamLead')")
	@PostMapping("/recordJoinTime")
	public ResponseEntity<String> recordJoinTime(@RequestParam String employeeId, @RequestParam String meetingLink) {
		try {
			// Log input values
			System.out.println("Employee ID: " + employeeId);
			System.out.println("Meeting Link: " + meetingLink);

			// Optionally, decode the meetingLink if needed
			// String decodedMeetingLink = URLDecoder.decode(meetingLink, "UTF-8");

			// Fetch employee entity
			Employee employee = employeeService.findById(employeeId);

			// Fetch session by meeting link
			Sessions session = employeeService.findByMeetingLink(meetingLink);

			if (employee != null && session != null) {
				employeeService.recordJoinTime(employee, session);
				return ResponseEntity.ok("Join time recorded successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Session not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request");
		}
	}

	@PreAuthorize("hasAnyRole('TeamLead')")
	@PostMapping("/recordLeaveTime")
	public ResponseEntity<String> recordLeaveTime(@RequestParam String employeeId, @RequestParam String meetingLink) {
		try {
			// Log the received parameters
			System.out.println("Received employeeId: " + employeeId);
			System.out.println("Received meetingLink: " + meetingLink);

			// Decode the meetingLink if necessary
			String decodedMeetingLink = java.net.URLDecoder.decode(meetingLink, "UTF-8");

			// Fetch employee and session
			Employee employee = employeeService.findById(employeeId);
			Sessions session = employeeService.findByMeetingLink(decodedMeetingLink);

			if (employee == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
			}

			if (session == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
			}

			// Record leave time
			employeeService.recordLeaveTime(employee, session);
			return ResponseEntity.ok("Leave time recorded successfully");

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
		}
	}

  @PreAuthorize("hasAnyRole('TeamLead')")
	@PutMapping("/updateSession/{id}")
	public ResponseEntity<Sessions> updateSession(@PathVariable("id") int id, @RequestBody Sessions updatedSession)
			throws Exception {
		Sessions session = employeeService.updateSession(id, updatedSession);
		if (session != null) {
			return ResponseEntity.ok(session);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
  
	@PreAuthorize("hasAnyRole('TeamLead')")
	@GetMapping("/getMeetingRecord")
	public ResponseEntity<EmployeeMeetingRecord> getMeetingRecord(@RequestParam String employeeId,
			@RequestParam String meetingLink) {
		EmployeeMeetingRecord recordDTO = employeeService.getMeetingRecord(employeeId, meetingLink);
		return ResponseEntity.ok(recordDTO);
	}
  
	class StartSessionRequest {
		private LocalDateTime startTime;
		private int sessionNumber;

		// Getters and setters
		public LocalDateTime getStartTime() {
			return startTime;
		}

		public void setSessionNumber(int sessionNumber) {
			this.sessionNumber = sessionNumber;
		}
	}
	@PreAuthorize("hasAnyRole('Developer', 'Tester')")
	@GetMapping("/getTotalTask")
	public ResponseEntity<List<Task>> getTotalTask(){
		List<Task> totalTask = employeeService.getTotalTask();
		return ResponseEntity.ok(totalTask);
		
	}	
}


