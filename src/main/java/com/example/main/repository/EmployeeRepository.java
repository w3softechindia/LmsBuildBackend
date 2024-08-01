package com.example.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {


	Employee findByEmployeeId(String employeeId);

//	@Query("DELETE FROM Employee e WHERE e.team.teamName = :teamName")
//	void deleteByTeamTeamName(@Param("teamName") String teamName);

	List<Employee> findByRolesRoleNameNot(String roleName);

	List<Employee> findByStatus(String status);

  List<Employee> findByRoles_RoleNameAndStatus(String roleName, String status);

  List<Employee> findByRoles_RoleNameNotAndStatus(String roleName, String status);
}


