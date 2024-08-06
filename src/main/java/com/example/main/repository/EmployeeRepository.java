package com.example.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
  @Query("SELECT e.employeeEmail FROM Employee e")
	List<String> findAllEmployeeEmails();

	@Query("SELECT e.webMail FROM Employee e")
	List<String> findAllWebMails();

	@Query("SELECT e.phoneNumber FROM Employee e")
	List<Long> findAllPhoneNumbers();

	boolean existsByEmployeeEmail(String employeeEmail);

	boolean existsByWebMail(String webMail);

	boolean existsByPhoneNumber(long phoneNumber);
	@Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.employeeEmail = :email AND e.employeeId <> :employeeId")
	Boolean existsByEmailAndNotEmployeeId(@Param("email") String email, @Param("employeeId") String employeeId);

	@Query("SELECT COUNT(e) > 0 FROM Employee e WHERE e.phoneNumber = :phoneNumber AND e.employeeId <> :employeeId")
	Boolean existsByPhoneNumberAndNotEmployeeId(@Param("phoneNumber") long phoneNumber,
			@Param("employeeId") String employeeId);
}


