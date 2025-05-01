package ru.example.JWT_Auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.enums.Role;
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

	@GetMapping
	public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
		return ResponseEntity.ok(adminService.getAllUsers());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdminUserDTO> getUserById(@PathVariable UUID id) {
		return ResponseEntity.ok(adminService.getUserById(id));
	}
	
	@GetMapping("/ids")
	public ResponseEntity<List<AdminUserDTO>> getUserById(@RequestBody List<UUID> id) {
		return ResponseEntity.ok(adminService.getUsersByid(id));
	}

	@PostMapping
	public ResponseEntity<AdminUserDTO> createUser(@RequestBody AdminUserDTO user) {
		return ResponseEntity.ok(adminService.saveUser(user));
	}

	@PutMapping("/{id}")
	public ResponseEntity<AdminUserDTO> updateUser(@PathVariable Long id, @RequestBody AdminUserDTO user) {
		return ResponseEntity.ok(adminService.updateUser(id, user));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
		adminService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/role")
	public ResponseEntity<AdminUserDTO> updateUserRole(@PathVariable UUID id, @RequestParam Role role) {
		return ResponseEntity.ok(adminService.updateUserRole(id, role));
	}

	@PatchMapping("/{id}/block")
	public ResponseEntity<AdminUserDTO> blockUser(@PathVariable UUID id) {
		return ResponseEntity.ok(adminService.blockUser(id));
	}

	@PatchMapping("/{id}/unblock")
	public ResponseEntity<AdminUserDTO> unblockUser(@PathVariable UUID id) {
		return ResponseEntity.ok(adminService.unblockUser(id));
	}

}
