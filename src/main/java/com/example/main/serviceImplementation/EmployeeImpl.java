package com.example.main.serviceImplementation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.service.EmployeeService;

import org.springframework.stereotype.Service;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;

@Service
public class EmployeeImpl implements EmployeeService {

import org.springframework.stereotype.Service;

import com.example.main.entity.Employee;
import com.example.main.repository.EmployeeRepository;
import com.example.main.service.EmployeeService;

@Service
public class EmployeeImpl implements EmployeeService{


	@Autowired
	private EmployeeRepository employeeRepository;


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CourseRepository courseRepository;


	@Override
	public Employee getEmployeeDetails(String employeeId) throws Exception {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception(" This Employee ID " + employeeId + " not found"));
		return employee;
	}

	@Override
	public Employee updateEmployeeDetails(String employeeId, Employee employee) throws Exception {

		Employee existingEmployee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee ID " + employeeId + " not found"));

		existingEmployee.setFirstName(employee.getFirstName());
		existingEmployee.setLastName(employee.getLastName());
		existingEmployee.setAddress(employee.getAddress());
		existingEmployee.setWebMail(employee.getWebMail());
		existingEmployee.setWebMailPassword(employee.getWebMailPassword());
		existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
		existingEmployee.setEmployeePassword(employee.getEmployeePassword());
		existingEmployee.setPhoneNumber(employee.getPhoneNumber());

    

		return employeeRepository.save(existingEmployee);
	}

	@Override
	public Employee resetPassword(String employeeId, String currentPassword, String newPassword) throws Exception {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee Id not found"));
		if (passwordEncoder.matches(currentPassword, employee.getPassword())) {
			employee.setEmployeePassword(passwordEncoder.encode(newPassword));
			employeeRepository.save(employee);
			return employee;
		}
		return employee;
	}

	@Override
	public Course getCourseByName(String courseName) throws Exception {


		return employeeRepository.save(existingEmployee);
	}


		Course getCourse = courseRepository.findById(courseName).orElseThrow(() -> new Exception("Course not found"));
		return getCourse;
	}
}
