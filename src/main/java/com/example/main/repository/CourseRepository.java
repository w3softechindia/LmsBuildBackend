package com.example.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {


	

}
