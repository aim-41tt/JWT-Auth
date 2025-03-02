package ru.example.JWT_Auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.example.JWT_Auth.DTO.request.RegisterRequest;
import ru.example.JWT_Auth.DTO.response.AuthenticationResponse;
import ru.example.JWT_Auth.service.AdminService;

@RestController
@RequestMapping("api/admin")
public class AdminController {

	private final AdminService adminService; 

	
	/**
	 * @param adminService
	 */
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}


	@PostMapping("/")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		AuthenticationResponse authenticationResponse = null;
		try {
			
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(e.getMessage());
		}
		return ResponseEntity.ok(authenticationResponse);
	}
	
}
