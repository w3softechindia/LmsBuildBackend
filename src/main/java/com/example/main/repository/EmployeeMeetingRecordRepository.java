package com.example.main.repository;

import com.example.main.entity.EmployeeMeetingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeMeetingRecordRepository extends JpaRepository<EmployeeMeetingRecord, Long> {

	EmployeeMeetingRecord findByEmployeeEmployeeIdAndSessionMeetingLink(String employeeId, String meetingLink);
}
