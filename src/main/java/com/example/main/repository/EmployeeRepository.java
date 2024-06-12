package com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

	Employee findByEmployeeId(String employeeId);

}
