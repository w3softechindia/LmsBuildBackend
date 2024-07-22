package com.example.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.Sessions;

@Repository
public interface SessionRepository extends JpaRepository<Sessions, Integer> {

}