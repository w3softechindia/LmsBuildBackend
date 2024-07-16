package com.example.main.serviceImplementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.emailUtil.EmailUtil;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Role;
import com.example.main.entity.Team;
import com.example.main.exception.InvalidAdminId;
import com.example.main.exception.InvalidIdException;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.RoleRepository;
import com.example.main.repository.TeamRepository;
import com.example.main.service.AdminService;

import jakarta.transaction.Transactional;

@Service
public class AdminImpl implements AdminService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private EmailUtil emailUtil;
	
	@Autowired
	private TeamRepository teamRepository;

	public String getEncodedPassword(String employeePassword) {
		return passwordEncoder.encode(employeePassword);
	}

	@SuppressWarnings("unused")
	private static final int MAX_IMAGE_SIZE = 1024 * 1024; // Example: 1 MB
	String uploadDir = "C:\\Users\\HP\\OneDrive\\Desktop\\Lms_Picture";
	

	public void sortEmployeesDescending(List<Employee> employees) {
		Collections.sort(employees, Comparator.comparing(Employee::getDateOfJoin).reversed());
	}
	
	@Override
	public String initRoleAndAdmin() {
		Role adminRole = new Role();
		adminRole.setRoleName("Admin");
		roleRepository.save(adminRole);
		
		Role teamleadRole = new Role();
		teamleadRole.setRoleName("TeamLead");
		roleRepository.save(teamleadRole);
		
		Role developerRole = new Role();
		developerRole.setRoleName("Developer");
		roleRepository.save(developerRole);
		
		Role testerRole = new Role();
		testerRole.setRoleName("Tester");
		roleRepository.save(testerRole);
		
		return "Success";
	}
	
	@Override
	public Employee addAdmin(Employee admin) {
		
		Role role = roleRepository.findById("Admin").get();
		Set<Role> adminRole = new HashSet<>();
		adminRole.add(role);
		admin.setRoles(adminRole);
		String encodedPassword = getEncodedPassword(admin.getEmployeePassword());
		admin.setEmployeePassword(encodedPassword);
		return employeeRepository.save(admin);
	}
	public Course addCourse(Course course) {
		Course save = courseRepository.save(course);
		return save;
	}

	@Override
	public List<Course> getAllCourseDetails() {
		List<Course> list = courseRepository.findAll();
		return list;
	}

	@Override
	public Course getCourseById(String courseId) throws InvalidIdException {
Optional<Course> course = courseRepository.findById(courseId);
		return course.get();
	}


//	@Override
//	public Employee addEmployee(Employee employee, String roleName) throws Exception {
//		Role role = roleRepository.findById(roleName).get();
//		Set<Role> employeeRole = new HashSet<>();
//		employeeRole.add(role);
//		employee.setRoles(employeeRole);
//		String encodedPassword = getEncodedPassword(employee.getEmployeePassword());
//		employee.setEmployeePassword(encodedPassword);
//		emailUtil.sendMail(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(), employee.getWebMail(), employee.getWebMailPassword(), roleName, employee.getPhoneNumber(), employee.getEmployeePassword(), employee.getEmployeeEmail());
//		return employeeRepository.save(employee);
//		
//	}

	@Override
	public Employee addEmployee(Employee employee, String roleName) throws Exception {
	    Role role = roleRepository.findById(roleName).get();
	    Set<Role> employeeRole = new HashSet<>();
	    employeeRole.add(role);
	    employee.setRoles(employeeRole);

	    // Store the original password before encoding
	    String originalPassword = employee.getEmployeePassword();

	    // Encode the password and set it to the employee object
	    String encodedPassword = getEncodedPassword(originalPassword);
	    employee.setEmployeePassword(encodedPassword);

	    // Send the original password in the email
	    emailUtil.sendMail(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(), 
	        employee.getWebMail(), employee.getWebMailPassword(), roleName, 
	        employee.getPhoneNumber(), employee.getDateOfJoin(), originalPassword, employee.getEmployeeEmail());

	    // Save the employee with the encoded password
	    return employeeRepository.save(employee);
	}

	@Override
	public Employee getAdmin(String employeeId) throws InvalidAdminId {
		
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		if(employee.isEmpty()) {
			throw new InvalidAdminId("give Valid AdminId");
		}else {
		return employee.get();
	}
		
	}
		


	@Override
	public Employee updateAdmin(String employeeId, Employee employee) throws InvalidAdminId 
	{
//		Employee employee1 = employeeRepository.findById(employeeId).orElseThrow(()->new InvalidAdminId("Give valid AdminId"));
//		
//		Employee employee2=employee1;
//		employee2.setAddress(employee.getAddress());
//		employee2.setEmployeeEmail(employee.getEmployeeEmail());
//		employee2.setEmployeePassword(employee.getEmployeePassword());
//		employee2.setFirstName(employee.getFirstName());
//		employee2.setLastName(employee.getLastName());
//		employee2.setPhoneNumber(employee.getPhoneNumber());
//		
//		Employee save = employeeRepository.save(employee2);
//		return save;
		 Employee employee1 = employeeRepository.findById(employeeId)
		            .orElseThrow(() -> new InvalidAdminId("Give valid AdminId"));

		    if (employee.getAddress() != null) {
		        employee1.setAddress(employee.getAddress());
		    }
		    if (employee.getEmployeeEmail() != null) {
		        employee1.setEmployeeEmail(employee.getEmployeeEmail());
		    }
		    if (employee.getEmployeePassword() != null) {
		    	String encodedPassword = getEncodedPassword(employee.getEmployeePassword());
		        employee1.setEmployeePassword(encodedPassword);
		    }
		    if (employee.getWebMail() != null) {
		        employee1.setWebMail(employee.getWebMail());
		    }
		    if (employee.getWebMailPassword() != null) {
		        employee1.setWebMailPassword(employee.getWebMailPassword());
		    }
		    if (employee.getFirstName() != null) {
		        employee1.setFirstName(employee.getFirstName());
		    }
		    if (employee.getLastName() != null) {
		        employee1.setLastName(employee.getLastName());
		    }
		    if (employee.getPhoneNumber() != 0) { // Assuming 0 is the default invalid value for phoneNumber
		        employee1.setPhoneNumber(employee.getPhoneNumber());
		    }

		    return employeeRepository.save(employee1);
	}

//	@Override
//	public Employee resetPassword(String employeeId, String currentPassword, String newPassword) throws InvalidAdminId, InvalidPassword {
//		Employee employee = employeeRepository.findById(employeeId)
//				.orElseThrow(() -> new InvalidAdminId("Employee Id not found"));
//		if (passwordEncoder.matches(currentPassword, employee.getPassword())) {
//			employee.setEmployeePassword(passwordEncoder.encode(newPassword));
//			employeeRepository.save(employee);
//			return employee;
//			
//		}
//		else {
//			throw new InvalidPassword("Entered Password is Invalid");
//		}
//		
//	}


	 @Override
		public List<Employee> getAllEmployee() {
	    
			List<Employee> list = employeeRepository.findAll();
			
			return list;
		}

	   
		
		@Override
		    public List<Employee> getEmployeesByRole(String roleName) {
		               
		         List<Employee> filteredEmployees = employeeRepository.findAll().stream()
		                 .filter(emp -> emp.getRoles().stream()
		                         .anyMatch(role -> role.getRoleName().equalsIgnoreCase(roleName)))
		                 .collect(Collectors.toList());
		    	 Collections.sort(filteredEmployees, (e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName()));
		        		return filteredEmployees;
		    }



		@Override
		public List<Employee> getEmployeesNotAdmin() {
		List<Employee> list = employeeRepository.findByRolesRoleNameNot("Admin");
		 Collections.sort(list, (e1, e2) -> e1.getFirstName().compareTo(e2.getFirstName()));	
		return list;
		}
	    	
		

		@Override
		public int getEmployeesNoByRole(String roleName) {
			List<Employee> filteredEmployees = employeeRepository.findAll().stream()
					.filter(emp -> emp.getRoles().stream().anyMatch(role -> role.getRoleName().equalsIgnoreCase(roleName)))
					.collect(Collectors.toList());
			return filteredEmployees.size();
		}

		@Override
		public int getTotalCourses() {
			List<Course> list = courseRepository.findAll();
			return list.size();
		}

		@Override
		public String updateEmployeeStatus(String employeeId, String status) throws InvalidIdException {
			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new InvalidIdException("Give valid EmployeeId"));
			if (status != null) {
				employee.setStatus(status);
			}
			employeeRepository.save(employee);
			return "status is updated to " + status + " successfully";

		}

		@Override
		public List<Team> getAllTeams() {

			List<Team> list = teamRepository.findAll();

			return list;
		}

		@Override
		public int getTotalTeams() {
			List<Team> allTeams = getAllTeams();
			return allTeams.size();
		}

		@Override
		public List<Employee> getEmployeesByRoleAfterStatus(String roleName) {
			List<Employee> byRoles_RoleNameAndStatus = employeeRepository.findByRoles_RoleNameAndStatus(roleName,
					"Approved");
			return byRoles_RoleNameAndStatus;
		}

		@Override
		public List<Employee> getEmployeesNotAdminAfterStatus() {
			List<Employee> list = employeeRepository.findByStatus("Approved");
//			List<Employee> list = employeeRepository.findByRoles_RoleNameNotAndStatus("Admin","Approved");
//		Collections.sort(list, (e1, e2) -> e1.getDateOfJoin().compareTo(e2.getDateOfJoin()));
//			sortEmployeesDescending(list);
			return list;
		}

		@Override
		public byte[] getProfilePicture(String employeeId) throws IOException {
			// Fetch the user entity by email
			Employee employee = employeeRepository.findByEmployeeId(employeeId);
			if (employee == null) {
				throw new IllegalArgumentException("User with employeeId does not exist.");
			}

			// Get the image path from the user object
			String imagePath = employee.getImagePath();
			if (imagePath == null || imagePath.isEmpty()) {
				throw new IllegalArgumentException("User with employeeId does not have a photo.");
			}

			// Read the photo bytes from the file
			Path photoPath = Paths.get(imagePath);
			return Files.readAllBytes(photoPath);
		}

		@Override
		@Transactional
		public void updatePhoto(String employeeId, MultipartFile photo) throws Exception {
			// Fetch the user from the database
			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new Exception("employee not found"));
			if (employee != null) {

				// Delete the old photo from the folder
				deletePhotoFromFileSystem(employee.getImagePath());

				// Save the new photo to the folder and update the user's photo path in the
				// database

				String imagePath = savePhotoToFileSystem(photo);
				employee.setImagePath(imagePath);
				employeeRepository.save(employee);
			} else {
				throw new IllegalArgumentException("User with employee does not exist.");
			}

		}

		private void deletePhotoFromFileSystem(String imagePath) throws IOException {
			if (imagePath != null) {
				Path path = Paths.get(imagePath);
				Files.deleteIfExists(path);
			}
		}

		private String savePhotoToFileSystem(MultipartFile photo) throws IOException {
			// Generate a unique filename for the new photo
			String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
			// Define the upload directory
			// Create the directory if it doesn't exist
			Path directoryPath = Paths.get(uploadDir);
			Files.createDirectories(directoryPath);
			// Save the photo to the upload directory
			Path filePath = Paths.get(uploadDir, fileName);
			Files.write(filePath, photo.getBytes());
			return filePath.toString();
		}

		@Override
		@Transactional
		public void uploadPhoto(String employeeId, MultipartFile file) throws Exception {
			// Generate a unique filename
			String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

			if (file.isEmpty()) {
				throw new IllegalArgumentException("File is empty");
			}

			// Save the image file to a local directory

			Path directoryPath = Paths.get(uploadDir);
			Files.createDirectories(directoryPath);

			String filePath = Paths.get(uploadDir, uniqueFileName).toString();
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			// Store the image path in the database
			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new Exception("Employee not found"));
			;
			if (employee != null) {
				employee.setImagePath(filePath);
				employeeRepository.save(employee);
			} else {
				throw new IllegalArgumentException("User with employee id does not exist.");
			}

		}

	}


	
   
	


