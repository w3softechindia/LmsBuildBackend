package com.example.main.service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Team;
import com.example.main.exception.InvalidAdminId;
import com.example.main.exception.InvalidIdException;

public interface AdminService {
	
	String initRoleAndAdmin();
	
	public Employee addEmployee(Employee employee,String roleName) throws Exception;
	
	public Employee addAdmin(Employee admin);
	public Employee getAdmin(String employeeId) throws InvalidAdminId;
	public Employee updateAdmin(String employeeId,Employee employee) throws InvalidAdminId;
//	public Employee resetPassword(String employeeId,String currentPassword,String  newPassword) throws InvalidAdminId, InvalidPassword;
	public List<Employee> getAllEmployee();
	public List<Employee> getEmployeesNotAdmin();
	public List<Employee>getEmployeesByRole(String roleName );
	public Course addCourse(Course course);
	public List<Course> getAllCourseDetails();
	public Course getCourseById(String CourseId) throws InvalidIdException;
	
	public int getEmployeesNoByRole(String roleName );

	public int getTotalCourses();

	public String updateEmployeeStatus(String employeeId, String status) throws InvalidIdException, InvalidAdminId;

	public List<Team> getAllTeams();

	public int getTotalTeams();

	List<Employee> getEmployeesNotAdminAfterStatus();


	List<Employee> getEmployeesByRoleAfterStatus(String roleName);

	byte[] getProfilePicture(String employeeId) throws IOException;

	void updatePhoto(String employeeId, MultipartFile photo) throws Exception;

	void uploadPhoto(String employeeId, MultipartFile file) throws Exception;
	
	
//	public Boolean checkEmail(String email);
//
//	public Boolean checkWebMail(String webMail);
//
//	public Boolean checkPhoneNumber(long phoneNumber);
//
//	List<String> getAllEmployeeEmails();
//
//	List<String> getAllWebMails();
//
//	List<Long> getAllPhoneNumbers();
//
//	Boolean checkEmailToUpdate(String employeeId,String email);
//
//	Boolean checkPhoneNumberToUpdate(String employeeId,long phoneNumber);
//

}
