package com.example.main.serviceImplementation;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.main.emailUtil.EmailUtil;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Role;
import com.example.main.exception.InvalidAdminId;
import com.example.main.exception.InvalidIdException;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.RoleRepository;
import com.example.main.service.AdminService;

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

	public String getEncodedPassword(String employeePassword) {
		return passwordEncoder.encode(employeePassword);
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
	        employee.getPhoneNumber(), originalPassword, employee.getEmployeeEmail());

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
	    	

	
   
	

}
