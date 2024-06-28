package com.example.main.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.exception.InvalidAdminId;
import com.example.main.service.AdminService;
import com.example.main.service.EmployeeService;

import jakarta.annotation.PostConstruct;

@RestController
@CrossOrigin("*")
public class AdminController {

	@Autowired
	private AdminService adminService;
  
	@Autowired
	private EmployeeService employeeService;

	private static final String UPLOAD_DIR = "./uploads";
  
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

//	@PreAuthorize("hasRole('Admin')")
//	@PutMapping("/resetPassword/{employeeId}/{currentPassword}/{newPassword}")
//	public  void resetPassword(@PathVariable String employeeId,@PathVariable String currentPassword,
//			@PathVariable String newPassword) throws InvalidAdminId, InvalidPassword {
//
//	 adminService.resetPassword(employeeId, currentPassword, newPassword);
//		
//	}

}
