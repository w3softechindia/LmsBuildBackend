package com.example.main.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Team;
import com.example.main.exception.InvalidAdminId;
import com.example.main.exception.InvalidIdException;
import com.example.main.service.AdminService;

import jakarta.annotation.PostConstruct;

@RestController
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;
  
	
	@PostConstruct
	public void initRoleAndAdmin() {
		adminService.initRoleAndAdmin();
	}
//	@Value("${file.upload-dir}")
//    private String uploadDir; // Inject the upload directory path from application.properties
//
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().body("Please select a file to upload.");
//        }
//
//        try {
//            // Generate unique file name
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//
//            // Resolve and create the target path
//            Path targetPath = (Path) Paths.get(uploadDir).resolve(fileName);
//
//            // Save the file to the target path
//            file.transferTo(((java.nio.file.Path) targetPath).toFile());
//
//            // Return file download URL
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/api/files/download/")
//                    .path(fileName)
//                    .toUriString();
//
//            return ResponseEntity.ok().body(fileDownloadUri);
//        } catch (IOException ex) {
//            // Handle file processing errors
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
//        }
//    }
//
//    @GetMapping("/download/{fileName:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
//        // Load file as Resource
//        Path filePath = (Path) Paths.get(uploadDir).resolve(fileName).normalize();
//        try {
//            Resource resource = new UrlResource(filePath.toUri());
//            if (((File) resource).exists() || ((Object) resource).isReadable()) {
//                return ResponseEntity.ok()
//                        .contentType(MediaType.parseMediaType(Files.probeContentType((java.nio.file.Path) filePath)))
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                        .body(resource);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (IOException ex) {
//            // Handle file retrieval errors
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

	@PostMapping("/addAdmin")
	public ResponseEntity<Employee> addAdmin(@RequestBody Employee admin) {

		Employee addAdmin = adminService.addAdmin(admin);
		return new ResponseEntity<Employee>(addAdmin, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getCourselist")
	public ResponseEntity<List<Course>> getAllCourses() {
		List<Course> courses = adminService.getAllCourseDetails();
		return new ResponseEntity<List<Course>>(courses, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getAdmin/{employeeId}")
	public ResponseEntity<Employee> getAdmin(@PathVariable String employeeId) throws InvalidAdminId {
		Employee admin = adminService.getAdmin(employeeId);
		return new ResponseEntity<Employee>(admin, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@PutMapping("/updateAdmin/{employeeId}")
	public ResponseEntity<Employee> updateAdmin(@PathVariable String employeeId, @RequestBody Employee employee)
			throws InvalidAdminId {
		Employee admin = adminService.updateAdmin(employeeId, employee);
		return new ResponseEntity<Employee>(admin, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@PostMapping("/addEmployee/{roleName}")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee, @PathVariable String roleName) throws Exception {

		Employee addEmployee = adminService.addEmployee(employee, roleName);
		return new ResponseEntity<Employee>(addEmployee, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getEmployeeList")
	public ResponseEntity<List<Employee>> getAllEmployee() throws InvalidAdminId {
		List<Employee> employees = adminService.getAllEmployee();
		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/employees/byRole")
	public ResponseEntity<List<Employee>> getEmployeesByRole(@RequestParam String roleName) {
		List<Employee> employees = adminService.getEmployeesByRole(roleName);
		return ResponseEntity.ok(employees);
	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/employees/notAdmin")
	public ResponseEntity<List<Employee>> getEmployeesNotAdmin() {
		List<Employee> employees = adminService.getEmployeesNotAdmin();
		return ResponseEntity.ok(employees);

	}

	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/employeesNumber/byRole/{roleName}")
	public ResponseEntity<Integer> getEmployeesNoByRole(@PathVariable String roleName) {
		int  employees = adminService.getEmployeesNoByRole(roleName);
		return ResponseEntity.ok(employees);
	}
@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getTotalCourses")
	public ResponseEntity<Integer> getTotalCourses() {
		int courses = adminService.getTotalCourses();
		return new ResponseEntity<Integer>(courses, HttpStatus.OK);
	}
  @PreAuthorize("hasRole('Admin')")
    @PutMapping("/updateEmployeeStatus/{employeeId}")
    public ResponseEntity<String> updateEmployeeStatus(@PathVariable String employeeId, @RequestBody String status)
            throws InvalidAdminId, InvalidIdException {
        try {
            String result = adminService.updateEmployeeStatus(employeeId, status);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (InvalidAdminId | InvalidIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while updating employee status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getAllTeam")
	public ResponseEntity<List<Team>> getAllTeams() {
		List<Team> teams = adminService.getAllTeams();
		return ResponseEntity.ok(teams);

	}
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getTotalTeams")
	public ResponseEntity<Integer> getTotalTeams() {
		int teams = adminService.getTotalTeams();
		return ResponseEntity.ok(teams);

	}
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/employeesAfterStatus/notAdmin")
	public ResponseEntity<List<Employee>> getEmployeesNotAdminAfterStatus() {
		List<Employee> employees = adminService.getEmployeesNotAdminAfterStatus();
		return ResponseEntity.ok(employees);

	}
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/employeesAfterStatus/byRole")
	public ResponseEntity<List<Employee>> getEmployeesByRoleAfterStatus(@RequestParam String roleName) {
		List<Employee> employees = adminService.getEmployeesByRoleAfterStatus(roleName);
		return ResponseEntity.ok(employees);
	}
@PreAuthorize("hasRole('Admin')")
	@PostMapping("/uploadPhotoAdmin/{employeeId}")
	public ResponseEntity<String> uploadPhoto(@PathVariable String employeeId, @RequestParam("file") MultipartFile file) throws Exception {
		try {
		adminService.uploadPhoto(employeeId, file);
			return ResponseEntity.ok("Photo uploaded successfully for user with employeeId ");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading photo: " + e.getMessage());
		}
	}
	
	@PreAuthorize("hasRole('Admin')")
	@GetMapping("/getPhotoAdmin/{employeeId}")
	public ResponseEntity<ByteArrayResource> getPhoto(@PathVariable String employeeId) {
		try {
			// Get the photo bytes for the given email
			byte[] photoBytes = adminService.getProfilePicture(employeeId);

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
	

}
