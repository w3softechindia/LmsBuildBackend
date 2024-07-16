package com.example.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.main.entity.Course;
import com.example.main.entity.Employee;
import com.example.main.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

	public List<Team> findByTeamLeadId(String employeeId);

	@Query("SELECT DISTINCT team.course FROM Team team JOIN team.employee e WHERE e.employeeId = :employeeId")
	List<Course> findCoursesByEmployeeId(@Param("employeeId") String employeeId);


}
