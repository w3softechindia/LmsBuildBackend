package com.example.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.main.entity.Employee;

import jakarta.transaction.Transactional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

	Employee findByEmployeeId(String employeeId);

	@Modifying
	@Transactional
	@Query("DELETE FROM Employee e WHERE e.team.teamName = :teamName")
	void deleteByTeamTeamName(@Param("teamName") String teamName);

	List<Employee> findByRolesRoleNameNot(String roleName);
}
