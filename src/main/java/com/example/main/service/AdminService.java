package com.example.main.service;


import java.util.List;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.exception.InvalidAdminId;
import com.example.main.exception.InvalidIdException;
import com.example.main.exception.InvalidPassword;

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
}
