package com.example.main.serviceImplementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Course addCourse(Course course) throws Exception {

//		if (courseRepository.existsById(course.getCourseName())) {
//			throw new Exception("Course already exists");
//		}
		Course course2 = courseRepository.save(course);
		for (SubCourse subCourse : course.getSubCourses()) {
			subCourse.setCourse(course);
			subCourseRepository.save(subCourse);
		}

		course2.setSubCourses(course.getSubCourses());
		return course2;
	}

	@Override
	public Team addTeamToEmployee(Team team, String employeeId) throws Exception {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new Exception("Employee not found "));
		Team team2 = teamRepository.save(team);

		team2.setEmployee(employee);
		teamRepository.save(team);
		return team;

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
		for(Course course2:team.getCourse()) {
			Course course3 = courseRepository.findById(course2.getCourseName()).orElseThrow(() -> new ResourceNotFound("Course ID not found"));
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

}
