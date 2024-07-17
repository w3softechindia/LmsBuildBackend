package com.example.main.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
	 List<Task> findByTeam_TeamName(String teamName);
}
