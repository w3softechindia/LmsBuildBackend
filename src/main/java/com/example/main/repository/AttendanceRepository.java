package com.example.main.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	 Set<Attendance> findByEmployee_EmployeeId(String employeeId);
	 	 
	long countByEmployee_EmployeeIdAndIsPresent(String employeeId, boolean b);
	
	 List<Attendance> findByEmployeeId(String employeeId);
}
	