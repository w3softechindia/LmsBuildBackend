package com.example.main.serviceImplementation;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.main.entity.Employee;
import com.example.main.entity.Role;

import com.example.main.repository.EmployeeRepository;
import com.example.main.repository.RoleRepository;
//import com.example.main.entity.Admin;
//import com.example.main.repository.AdminRepository;
import com.example.main.service.AdminService;

@Service
public class AdminImpl implements AdminService {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public String getEncodedPassword(String employeePassword) {
		return passwordEncoder.encode(employeePassword);
	}

	
	@Override
	public String initRoleAndAdmin() {
		Role adminRole = new Role();
		adminRole.setRoleName("Admin");
		roleRepository.save(adminRole);
		
		Role teamleadRole = new Role();
		teamleadRole.setRoleName("TeamLead");
		roleRepository.save(teamleadRole);
		
		Role developerRole = new Role();
		developerRole.setRoleName("Developer");
		roleRepository.save(developerRole);
		
		Role testerRole = new Role();
		testerRole.setRoleName("Tester");
		roleRepository.save(testerRole);
		
		return "Success";
	}
	
	@Override
	public Employee addAdmin(Employee admin) {
//		Role role = roleRepository.findById("Admin").get();
//		Set<Role> AdminRole = new HashSet<>();
//		AdminRole.add(role);
//
//		String encodedPassword = getEncodedPassword(admin.getPassword());
//		admin.setPassword(encodedPassword);
//		admin.setRole(Admin);
//		return adminRepository.save(admin);
		
		Role role = roleRepository.findById("Admin").get();
		Set<Role> adminRole = new HashSet<>();
		adminRole.add(role);
		admin.setRole(adminRole);
		String encodedPassword = getEncodedPassword(admin.getEmployeePassword());
		admin.setEmployeePassword(encodedPassword);
		return employeeRepository.save(admin);
	}


	@Override
	public Employee addEmployee(Employee employee, String roleName) {
		Role role = roleRepository.findById(roleName).get();
		Set<Role> employeeRole = new HashSet<>();
		employeeRole.add(role);
		employee.setRole(employeeRole);
		String encodedPassword = getEncodedPassword(employee.getEmployeePassword());
		employee.setEmployeePassword(encodedPassword);
		return employeeRepository.save(employee);
		
	}


	

	

}
