package com.example.main.serviceImplementation;

import java.io.IOException;
import java.time.LocalDate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.dto.SessionsDTO;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Role;
import com.example.main.entity.Sessions;
import com.example.main.entity.SubCourse;
import com.example.main.entity.Task;
import com.example.main.entity.Team;
import com.example.main.exception.ResourceNotFound;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.SessionRepository;
import com.example.main.repository.SubCourseRepository;
import com.example.main.repository.TaskRepository;
import com.example.main.repository.TeamRepository;
import com.example.main.service.TeamLeadService;

import jakarta.transaction.Transactional;

@Service
public class TeamLeadImplementation implements TeamLeadService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private SubCourseRepository subCourseRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private TaskRepository taskRepository;
  
	@Autowired
	private SessionRepository sessionRepository;

	@SuppressWarnings("unused")
	private static final int MAX_IMAGE_SIZE = 1024 * 1024; // Example: 1 MB
	String uploadDir = "C:\\Users\\91910\\Desktop\\LMS_PROJECT\\Photos";

	@Override
	public Employee getTeamLead(String employeeId) throws Exception {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee Id not found"));
		return employee;
	}

	@SuppressWarnings("unlikely-arg-type")
	@Override
	public List<Employee> getAllEmployees() {

		List<Employee> all = employeeRepository.findAll();
		List<Employee> teamLead = new ArrayList<>();

		for (Employee employee : all) {
			Set<Role> roles = employee.getRoles(); // Assuming roles are stored as a Set of Strings
			if (roles.contains("TeamLead")) {
				teamLead.add(employee);
			}
		}

		return teamLead;
	}

	@Override
	public Course addCourse(Course course, List<SubCourse> subCourses) throws Exception {

		Course savedCourse = courseRepository.save(course);

		for (SubCourse subCourse : subCourses) {
			subCourse.setCourse(savedCourse);
			subCourseRepository.save(subCourse);
		}

		savedCourse.setSubCourses(subCourses);
		return courseRepository.save(savedCourse);
	}

	@Override
	public Team addTeamToEmployee(Team team, String teamleadId) throws Exception {
		Employee teamlead = employeeRepository.findById(teamleadId)
				.orElseThrow(() -> new ResourceNotFound("Team lead with ID " + teamleadId + " not found"));

		List<Employee> employees = new ArrayList<>();

		for (Employee emp : team.getEmployee()) {
			Employee employee = employeeRepository.findById(emp.getEmployeeId())
					.orElseThrow(() -> new ResourceNotFound("Employee with ID " + emp.getEmployeeId() + " not found"));
			employees.add(employee);
			employee.setTeam(team);
		}

		Set<Course> course = new HashSet<>();
		for (Course course2 : team.getCourse()) {
			Course course3 = courseRepository.findById(course2.getCourseName())
					.orElseThrow(() -> new ResourceNotFound("Course ID not found"));
			course.add(course3);
		}

		team.setCourse(course);
		team.setEmployee(employees);
		team.setTeamLeadId(teamlead.getEmployeeId());

		return teamRepository.save(team);
	}

	@Override
	public List<Team> getAllTeams(String employeeId) throws Exception {
		return teamRepository.findByTeamLeadId(employeeId);

	}

	@Override
	public Team getTeamByName(String teamName) throws Exception {
		return teamRepository.findById(teamName).orElseThrow(() -> new Exception("Team name not found"));
	}

	@Override
	public String deleteEmployeeFromTeam(String employeeId) throws Exception {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee not found with id: " + employeeId));

		Team team = employee.getTeam();
		if (team != null) {
			team.getEmployee().remove(employee);
			employee.setTeam(null);
			teamRepository.save(team);
			employeeRepository.save(employee);
			return "Employee deleted from the team successfully";
		} else {
			throw new Exception("Employee does not belong to any team");
		}
	}

	@Override
	public Team updateTeam(String teamName, Team updatedTeam) throws Exception {
		Team existingTeam = teamRepository.findById(teamName)
				.orElseThrow(() -> new ResourceNotFound("Team with name " + teamName + " not found"));
		existingTeam.setMeetingLink(updatedTeam.getMeetingLink());
		if (updatedTeam.getCourse() != null && !updatedTeam.getCourse().isEmpty()) {
			Course newCourse = updatedTeam.getCourse().iterator().next();
			Course foundCourse = courseRepository.findById(newCourse.getCourseName()).orElseThrow(
					() -> new ResourceNotFound("Course with name " + newCourse.getCourseName() + " not found"));
			Set<Course> updatedCourses = new HashSet<>();
			updatedCourses.add(foundCourse);
			
			existingTeam.setCourse(updatedCourses);
		}
		if (updatedTeam.getEmployee() != null) {
			List<Employee> updatedEmployees = new ArrayList<>();
			for (Employee emp : updatedTeam.getEmployee()) {
				Employee employee = employeeRepository.findById(emp.getEmployeeId()).orElseThrow(
						() -> new ResourceNotFound("Employee with ID " + emp.getEmployeeId() + " not found"));
				updatedEmployees.add(employee);
				employee.setTeam(existingTeam);
			}
			existingTeam.setEmployee(updatedEmployees);
		}

		return teamRepository.save(existingTeam);
	}

	@Override
	public String deleteTeam(String teamName) throws Exception {
		Team team = teamRepository.findById(teamName)
				.orElseThrow(() -> new ResourceNotFound("Team with name " + teamName + " not found"));

		List<Employee> employees = team.getEmployee();
		for (Employee employee : employees) {
			employee.setTeam(null);
			employeeRepository.save(employee);
		}

		Set<Course> courses = team.getCourse();
		for (Course course : courses) {
			team.getCourse().remove(course);
		}
		teamRepository.delete(team);
		return "Team deleted successfully";
	}

	@Override
	public Set<Course> getCourses(String employeeId) throws Exception {
		List<Team> byTeamLeadId = teamRepository.findByTeamLeadId(employeeId);
		Set<Course> courses = new HashSet<Course>();
		for (Team team : byTeamLeadId) {
			Set<Course> course = team.getCourse();
			courses.addAll(course);
		}
		return courses;
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
	public List<Course> getAllCourses() throws Exception {
		List<Course> all = courseRepository.findAll();
		return all;
	}

	@Override
	public long getTotalTeamsByTeamLead(String employeeId) {
		List<Team> teamLeadId = teamRepository.findByTeamLeadId(employeeId);
		return teamLeadId.size();
	}

	@Override
	public List<Task> getTasksByTeamlead(String teamName) throws Exception {
		List<Task> list = taskRepository.findByTeam_TeamName(teamName);
		return list;
	}
	
	
	@Override
    public List<Sessions> createSessions(String teamName, String subCourseName, int numberOfSessions, List<LocalDate> dates, SessionsDTO sessionDTO) {
        Optional<Team> teamOptional = teamRepository.findById(teamName);
        if (teamOptional.isEmpty()) {
            throw new IllegalArgumentException("Team not found: " + teamName);
        }

        Team team = teamOptional.get();

        // Fetch the SubCourse based on subCourseName
        Optional<SubCourse> subCourseOptional = subCourseRepository.findById(subCourseName);
        if (subCourseOptional.isEmpty()) {
            throw new IllegalArgumentException("SubCourse not found: " + subCourseName);
        }

        SubCourse subCourse = subCourseOptional.get();

        if (dates.size() != numberOfSessions) {
            throw new IllegalArgumentException("Number of dates does not match the number of sessions");
        }

        List<Sessions> sessionsList = new ArrayList<>();
        for (int i = 0; i < numberOfSessions; i++) {
            Sessions session = new Sessions();
            session.setClassDate(dates.get(i));
            session.setStartTime(sessionDTO.getStartTime());
            session.setEndTime(sessionDTO.getEndTime());

            // Calculate class duration based on start and end time
            Duration duration = Duration.between(sessionDTO.getStartTime(), sessionDTO.getEndTime());
            int classDuration = (int) duration.toMinutes();
            session.setClassDuration(classDuration);

            session.setMeetingLink(sessionDTO.getMeetingLink());
            session.setClassStatus(sessionDTO.getClassStatus());
            session.setSessionNumber(i + 1); // Set session number starting from 1
            session.setTeam(team);
            session.setSubCourse(subCourse);

            sessionsList.add(session);
        }

        return sessionRepository.saveAll(sessionsList);
    }

//	@Override
//	public Attendance createAttendance(int classId, String employeeId, LocalDateTime startTime, LocalDateTime endTime) throws Exception {
//	    // Fetch session and employee
//	    Sessions session = sessionRepository.findById(classId)
//	            .orElseThrow(() -> new RuntimeException("Session not found"));
//	    Employee employee = employeeRepository.findById(employeeId)
//	            .orElseThrow(() -> new Exception("Employee not found"));
//
//	    // Validate session times
//	    LocalDateTime sessionStartTime = session.getStartTime();
//	    LocalDateTime sessionEndTime = session.getEndTime();
//	    if (sessionStartTime == null || sessionEndTime == null) {
//	        throw new RuntimeException("Session times are not properly set");
//	    }
//
//	    // Calculate total meeting duration
//	    long totalMeetingMinutes = Duration.between(sessionStartTime, sessionEndTime).toMinutes();
//
//	    // Validate attendance times
//	    if (startTime == null || endTime == null) {
//	        throw new RuntimeException("Attendance times cannot be null");
//	    }
//
//	    // Calculate the attendance duration
//	    long attendanceMinutes = Duration.between(startTime, endTime).toMinutes();
//
//	    // Determine if the employee was present for at least 75% of the meeting
//	    boolean isPresent = (attendanceMinutes >= totalMeetingMinutes * 0.75);
//
//	    // Create and save attendance record
//	    Attendance attendance = new Attendance();
//	    attendance.setEmployee(employee);
//	    attendance.setSession(session);
//	    attendance.setStartTime(startTime);
//	    attendance.setEndTime(endTime);
//	    attendance.setPresent(isPresent);
//
//	    return attendanceRepository.save(attendance);
//	}
//	@Override
//    public Attendance createAttendance(int classId, String employeeId, LocalDateTime startTime, LocalDateTime endTime) throws Exception {
//        Sessions session = sessionRepository.findById(classId)
//                .orElseThrow(() -> new RuntimeException("Session not found"));
//        Employee employee = employeeRepository.findById(employeeId)
//                .orElseThrow(() -> new Exception("Employee not found"));
//
//        if (session.getStartTime() == null || session.getEndTime() == null) {
//            throw new RuntimeException("Session times are not properly set");
//        }
//
//        session.setEmployee(employee);
//
//        Attendance attendance = new Attendance();
//        attendance.setEmployee(employee);
//        attendance.setSession(session);
//        attendance.setStartTime(startTime);
//        attendance.setEndTime(endTime);
//
//        Duration totalDuration = Duration.between(session.getStartTime(), session.getEndTime());
//        Duration attendedDuration = Duration.between(startTime, endTime);
//
//        double attendancePercentage = (double) attendedDuration.toMinutes() / totalDuration.toMinutes() * 100;
//        boolean isPresent = attendancePercentage >= 75;
//
//        attendance.setPresent(isPresent);
//
//        return attendanceRepository.save(attendance);
//    }
//	
//	@Override
//    public Set<Attendance> getAttendance(String employeeId) throws Exception {
//        // Verify that the employee exists
//        employeeRepository.findById(employeeId)
//            .orElseThrow(() -> new Exception("Employee not found"));
//
//        // Fetch and return the attendances for the given employeeId
//        return attendanceRepository.findByEmployee_EmployeeId(employeeId);
//    }
//
//	@Override
//	public void updateEmployeeAttendanceStatus(String employeeId) {
//        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
//
//        List<Attendance> attendances = attendanceRepository.findByEmployeeId(employeeId);
//        if (attendances.isEmpty()) {
//            employee.setAttendanceStatus("Absent");
//            employeeRepository.save(employee);
//            return;
//        }
//
//        int totalMeetingTimeMinutes = 0;
//        int attendedTimeMinutes = 0;
//
//        for (Attendance attendance : attendances) {
//            if (attendance.isPresent()) {
//                LocalDateTime startTime = attendance.getStartTime();
//                LocalDateTime endTime = attendance.getEndTime();
//                int durationMinutes = (int) java.time.Duration.between(startTime, endTime).toMinutes();
//                attendedTimeMinutes += durationMinutes;
//            }
//
//            Sessions session = attendance.getSession();
//            if (session != null) {
//                LocalDateTime sessionStart = session.getStartTime();
//                LocalDateTime sessionEnd = session.getEndTime();
//                int sessionDurationMinutes = (int) java.time.Duration.between(sessionStart, sessionEnd).toMinutes();
//                totalMeetingTimeMinutes += sessionDurationMinutes;
//            }
//        }
//
//        double attendancePercentage = ((double) attendedTimeMinutes / totalMeetingTimeMinutes) * 100;
//
//        if (attendancePercentage >= 75) {
//            employee.setAttendanceStatus("Present");
//        } else {
//            employee.setAttendanceStatus("Absent");
//        }
//
//        employeeRepository.save(employee);
//    }
}
