package com.example.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.SubCourse;
@Repository
public interface SubCourseRepository extends JpaRepository<SubCourse, String>{

	Optional<SubCourse> findBySubCourseName(String subCourseName);

}
