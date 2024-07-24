package com.example.main.entity;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Employee implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String employeeId;
	private String firstName;
	private String lastName;
	private String address;
	private String webMail;
	private String webMailPassword;
	private String employeeEmail;
	private String employeePassword;
	private long phoneNumber;
	private String imagePath;
	private String dateOfJoin;
	private String status;
	
	
	@Column(name = "image_bytes", columnDefinition = "LONGBLOB")
	private byte[] imageBytes;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "Employee_Roles",joinColumns = {@JoinColumn(name="Employee_Id")},inverseJoinColumns = {@JoinColumn(name="Role_Name")})
	private Set<Role> roles = new HashSet<>();
	
	@ManyToOne
	@JsonBackReference
	private Team team;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<Authority> authorities = new HashSet<>();
		 this.roles.forEach(userRole->{
			 authorities.add(new Authority("ROLE_"+userRole.getRoleName()));
		 });
	        return authorities;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.employeeId;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.employeePassword;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}