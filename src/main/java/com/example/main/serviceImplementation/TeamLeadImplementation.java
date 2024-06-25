package com.example.main.serviceImplementation;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Role;
import com.example.main.entity.SubCourse;
import com.example.main.entity.SubCourseRepository;
import com.example.main.entity.Team;
import com.example.main.exception.ResourceNotFound;
import com.example.main.repository.CourseRepository;
import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.TeamRepository;
import com.example.main.service.TeamLeadService;

@Service
public class TeamLeadImplementation implements TeamLeadService {

	private static final String UPLOAD_DIR = "uploads";

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private SubCourseRepository subCourseRepository;

	@Autowired
	private TeamRepository teamRepository;

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
	public List<Course> getAllCourses() throws Exception {
		List<Course> all = courseRepository.findAll();
		return all;
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
	    if (updatedTeam.getCourse() != null && !updatedTeam.getCourse().isEmpty()) {
	        Course newCourse = updatedTeam.getCourse().iterator().next();
	        Course foundCourse = courseRepository.findById(newCourse.getCourseName())
	                .orElseThrow(() -> new ResourceNotFound("Course with name " + newCourse.getCourseName() + " not found"));
	        Set<Course> updatedCourses = new HashSet<>();
	        updatedCourses.add(foundCourse);
	        existingTeam.setCourse(updatedCourses);
	    }
	    if (updatedTeam.getEmployee() != null) {
	        List<Employee> updatedEmployees = new ArrayList<>();
	        for (Employee emp : updatedTeam.getEmployee()) {
	            Employee employee = employeeRepository.findById(emp.getEmployeeId()).orElseThrow(() -> new ResourceNotFound("Employee with ID " + emp.getEmployeeId() + " not found"));
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
	 public byte[] uploadProfilePhoto(String employeeId, MultipartFile file) throws Exception, IOException {
	        Employee employee = employeeRepository.findById(employeeId)
	            .orElseThrow(() -> new Exception("Employee not found"));

	        // Generate a file name
	        String fileName = employeeId + "_" + file.getOriginalFilename();
	        Path filePath = Paths.get(UPLOAD_DIR, fileName);

	        // Save the file locally
	        Files.write(filePath, file.getBytes());

	        // Update employee's profile picture path
	        employee.setProfilePicture(filePath.toString());
	        employeeRepository.save(employee);

	        return file.getBytes();
	    }


}

