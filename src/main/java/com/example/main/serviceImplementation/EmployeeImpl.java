package com.example.main.serviceImplementation;


import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Task;
import com.example.main.entity.Team;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.TaskRepository;
import com.example.main.repository.TeamRepository;
import com.example.main.service.EmployeeService;
import com.example.main.entity.SubCourseRepository;


@Service
public class EmployeeImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TaskRepository taskRepository;

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
	public Set<Course> getCoursesByEmployeeId(String employeeId) throws Exception {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee ID not found"));
		return employee.getTeam().getCourse();
	}

	@Override
	public Course getCourseByCourseName(String courseName) {
		return courseRepository.findById(courseName).orElse(null);
	}

	@Override
	public List<Task> assignTasksToTeam(List<Task> tasks, String teamName) {

		Team team = teamRepository.findById(teamName)
				.orElseThrow(() -> new RuntimeException("Team not found with name: " + teamName));
		tasks.forEach(task -> task.setTeam(team));
		return taskRepository.saveAll(tasks);
	}

	@Override
	public List<Task> getTasksByEmployeeId(String employeeId) throws Exception {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(()-> new Exception("Employee not found...!!!"));
		Team team = teamRepository.findById(employee.getTeam().getTeamName()).orElseThrow(()-> new Exception("No Team found...!!!"));
		return team.getTask();
	}

	@Override
	public Course getCourseByCourseName(String courseName) {
		return courseRepository.findById(courseName).orElse(null);
    }

}


