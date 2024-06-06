package com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Team;
@Repository
public interface TeamRepository extends JpaRepository<Team, String>{

}
