package com.example.main.service;


import com.example.main.entity.Employee;

public interface AdminService {
	
	String initRoleAndAdmin();
	
	public Employee addEmployee(Employee employee,String roleName);
	
	public Employee addAdmin(Employee admin);
	
}
