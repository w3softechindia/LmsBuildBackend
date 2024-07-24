package com.example.main.serviceImplementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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


	 private final Path tempLocation = Paths.get("E:\\LMS_Backup_Folder\\TaskFiles");
	    private final Path finalLocation = Paths.get("E:\\LMS_Backup_Folder\\TaskFiles");

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

//	@Override
//	public Course updateCourseProgress(String courseName, int progress) {
//
//		Course course = courseRepository.findById(courseName)
//				.orElseThrow(() -> new RuntimeException("Course not found"));
//
//		int newProgress = Math.min(progress, 100);
//		course.setProgress(newProgress);
//
//		if (newProgress == 100) {
//
//			List<Course> allCourses = courseRepository.findAll();
//			for (int i = 0; i < allCourses.size(); i++) {
//				if (allCourses.get(i).getCourseName().equals(courseName) && i < allCourses.size() - 1) {
//					Course nextCourse = allCourses.get(i + 1);
//					nextCourse.setProgress(Math.min(nextCourse.getProgress() + 20, 100));
//					courseRepository.save(nextCourse);
//					break;
//				}
//			}
//
//		}
//		return courseRepository.save(course);
//
//	}

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

	@Override
	public void uploadTaskFile(String taskId, MultipartFile file) {
		  try {
	            if (file.isEmpty()) {
	                throw new RuntimeException("Failed to store empty file.");
	            }

	            // Create the temporary directory if it doesn't exist
	            if (!Files.exists(tempLocation)) {
	                Files.createDirectories(tempLocation);
	            }

	            // Store the file in the temporary directory
	            Path tempFile = tempLocation.resolve(Paths.get(file.getOriginalFilename()))
	                .normalize()
	                .toAbsolutePath();

	            Files.copy(file.getInputStream(), tempFile);

	            // Create the final directory if it doesn't exist
	            if (!Files.exists(finalLocation)) {
	                Files.createDirectories(finalLocation);
	            }

	            // Move the file to the final directory
	            Path finalFile = finalLocation.resolve(Paths.get(file.getOriginalFilename()))
	                .normalize()
	                .toAbsolutePath();

	            Files.move(tempFile, finalFile);

	            // Update the task entity with the file name
	            Task task = taskRepository.findById(taskId)
	                .orElseThrow(() -> new RuntimeException("Task not found"));
	            task.setFileName(file.getOriginalFilename());
	            taskRepository.save(task);

	        } catch (IOException e) {
	            throw new RuntimeException("Failed to store file.", e);
	        }
	    }

	@Override
	public Path getTaskFile(String taskId) {
		 Optional<Task> optionalTask = taskRepository.findById(taskId);
	        if (optionalTask.isPresent()) {
	            Task task = optionalTask.get();
	            Path filePath = this.finalLocation.resolve(task.getFileName()).normalize();
	            if (Files.exists(filePath)) {
	                return filePath;
	            } else {
	                throw new RuntimeException("File not found");
	            }
	        } else {
	            throw new RuntimeException("Task not found");
	        }
	    }
		
	}



//	@Override
//	public SubCourse updateSubCourseProgress(String subCourseName, int progress) {
//		SubCourse subCourse = subCourseRepository.findById(subCourseName)
//				.orElseThrow(() -> new RuntimeException("SubCourse not found"));
//
//		int newProgress = Math.min(progress, 100);
//		subCourse.setProgress(newProgress);
//
//
//		return subCourseRepository.save(subCourse);
//	}
//
//	@Override
//	public SubCourse updateSubCourseStatus(String SubCourseName, String status) throws Exception {
//		SubCourse subCourse = subCourseRepository.findById(SubCourseName)
//				.orElseThrow(() -> new Exception("SubCourse Name not found..!!"));
//
//		if (status.equals("Completed") || status.equals("InComplete")) {
//			subCourse.setStatus(status);
//			return subCourseRepository.save(subCourse);
//		} else {
//			throw new Exception("Only 'Completed' and 'InComplete' statuses are allowed");
//		}
//	}

//	@Override
//	public SubCourse createSubCourse(SubCourse subCourse) {
//		  subCourse = subCourseRepository.save(subCourse);
//	        int duration = subCourse.getSubCourseDuration();
//	        List<Sessions> sessionsList = new ArrayList<>();
//
//	        for (int i = 0; i < duration; i++) {
//	            Sessions session = new Sessions();
//	            session.setClassDuration(1);
//	            session.setClassDate(LocalDate.now().plusDays(i));
//	            session.setClassStatus(i % 2 == 0 ? "completed" : "incomplete");
//	            session.setSubCourse(subCourse);
//	            sessionsList.add(session);
//	        }
//
//	        sessionRepository.saveAll(sessionsList);
//	        subCourse.setSessions(sessionsList);
//	        return subCourse;
//	    }
