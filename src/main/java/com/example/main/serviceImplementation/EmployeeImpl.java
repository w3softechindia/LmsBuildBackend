package com.example.main.serviceImplementation;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Sessions;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Task;
import com.example.main.entity.Team;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.SessionRepository;
import com.example.main.repository.SubCourseRepository;
import com.example.main.repository.TaskRepository;
import com.example.main.repository.TeamRepository;
import com.example.main.service.EmployeeService;

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

	@Autowired
	private SubCourseRepository subCourseRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@SuppressWarnings("unused")
	private static final int MAX_IMAGE_SIZE = 1024 * 1024; // Example: 1 MB
	String uploadDir = "E:\\LMS_Backup_Folder\\Picture";

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
		existingEmployee.setEmployeeEmail(employee.getEmployeeEmail());
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
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee not found...!!!"));
		Team team = teamRepository.findById(employee.getTeam().getTeamName())
				.orElseThrow(() -> new Exception("No Team found...!!!"));
		return team.getTask();
	}

	@Override
	public Task updateTaskStatus(String taskId, String status) throws Exception {

		Task task = taskRepository.findById(taskId).orElseThrow(() -> new Exception("Task Id not found..!!"));

		if (status.equals("Completed") || status.equals("InComplete")) {
			task.setStatus(status);
			return taskRepository.save(task);
		} else {
			throw new Exception("Only Completed and InComplete allowded");
		}
	}

	@Override
	public Course updateCourseProgress(String courseName, int progress) {

		Course course = courseRepository.findById(courseName)
				.orElseThrow(() -> new RuntimeException("Course not found"));

		int newProgress = Math.min(progress, 100);
		course.setProgress(newProgress);

		if (newProgress == 100) {

			List<Course> allCourses = courseRepository.findAll();
			for (int i = 0; i < allCourses.size(); i++) {
				if (allCourses.get(i).getCourseName().equals(courseName) && i < allCourses.size() - 1) {
					Course nextCourse = allCourses.get(i + 1);
					nextCourse.setProgress(Math.min(nextCourse.getProgress() + 20, 100));
					courseRepository.save(nextCourse);
					break;
				}
			}

		}
		return courseRepository.save(course);

	}

	@Override
	public String getMeetingLinkByTeamName(String teamName) throws Exception {

		Team team = teamRepository.findById(teamName).orElseThrow(() -> new Exception("Team Name not found"));
		if (team != null) {
			return team.getMeetingLink();
		}
		return null;
	}

	@Override
	public SubCourse getSubCourseBySubName(String subCourseName) {
		return subCourseRepository.findById(subCourseName).orElse(null);
	}

	@Override
	public void markSessionAsAttended(int classId) {

		Sessions session = sessionRepository.findById(classId)
				.orElseThrow(() -> new RuntimeException("Session not found"));
		session.setClassStatus("Session attended");
		sessionRepository.save(session);

	}

	@Override
	public Team getTeamByEmployeeIdd(String employeeId) throws Exception {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee not found"));
		return employee.getTeam();
	}

}
