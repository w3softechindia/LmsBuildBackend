package com.example.main.security;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.main.entity.Employee;
import com.example.main.repository.EmployeeRepository;


@Service
public class JwtServiceImplementation implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtResponse createJwtToken(JwtRequest jwtRequest, boolean rememberMe) throws Exception {
        String employeeId = jwtRequest.getEmployeeId();
        String password = jwtRequest.getEmployeePassword();
        authenticate(employeeId, password);

        UserDetails userDetails = loadUserByUsername(employeeId);
        Optional<Employee> userOptional = employeeRepository.findById(employeeId);
        Employee user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
        
        String token = jwtUtil.generateToken(userDetails, rememberMe);
        return new JwtResponse(user, token);
    }

    @Override
    public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
        Employee user = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new UsernameNotFoundException("EmployeeId not found..!!!"));

        return new org.springframework.security.core.userdetails.User(user.getEmployeeId(), user.getEmployeePassword(),
                user.getAuthorities());
    }

    private void authenticate(String employeeId, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(employeeId, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}