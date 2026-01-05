package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.example.JWT_Auth.service.AdminService;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
//@RequestMapping("api/admin")
public class AdminController {

	private final AdminService adminService;

	/**
	 * @param adminService
	 */
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	/*
	 * временно пусто
	 */

}
