package com.example.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Course;
import com.example.main.entity.Employee;
@Repository
public interface CourseRepository extends JpaRepository<Course, String>{

//	List<Course> findAll(Optional<Employee> byId);
	

}
