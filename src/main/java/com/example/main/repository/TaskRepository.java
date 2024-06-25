package com.example.main.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
