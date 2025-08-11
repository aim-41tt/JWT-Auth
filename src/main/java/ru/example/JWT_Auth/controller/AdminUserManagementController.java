package ru.example.JWT_Auth.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.example.JWT_Auth.DTO.admin.AdminUserDTO;
import ru.example.JWT_Auth.model.enums.Role;
import ru.example.JWT_Auth.service.AdminService;

/**
 * Контроллер для управления пользователями со стороны администратора.
 * Здесь находятся все CRUD-операции, блокировка/разблокировка пользователей,
 * а также изменение их ролей и других данных.
 * 
 * Базовый путь: api/admin/users
 */
@RestController
@RequestMapping("api/admin/users")
public class AdminUserManagementController {

    private final AdminService adminService;

    public AdminUserManagementController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * Получить список всех пользователей.
     * @return список пользователей в виде AdminUserDTO
     */
    @GetMapping
    public ResponseEntity<List<AdminUserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Получить пользователя по ID.
     * @param id UUID пользователя
     * @return данные пользователя
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    /**
     * Получить список пользователей по их UUID.
     * @param ids список UUID пользователей
     * @return список найденных пользователей
     */
    @PostMapping("/ids")
    public ResponseEntity<List<AdminUserDTO>> getUsersById(@RequestBody List<UUID> ids) {
        return ResponseEntity.ok(adminService.getUsersByid(ids));
    }

    /**
     * Создать нового пользователя.
     * @param user данные пользователя
     * @return созданный пользователь
     */
    @PostMapping
    public ResponseEntity<AdminUserDTO> createUser(@RequestBody AdminUserDTO user) {
        return ResponseEntity.ok(adminService.saveUser(user));
    }

    /**
     * Обновить данные пользователя.
     * @param id ID пользователя (Long — возможно стоит сменить на UUID для единообразия)
     * @param user новые данные пользователя
     * @return обновленный пользователь
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdminUserDTO> updateUser(@PathVariable UUID id, @RequestBody AdminUserDTO user) {
        return ResponseEntity.ok(adminService.updateUser(id, user));
    }

    /**
     * Удалить пользователя по ID.
     * @param id UUID пользователя
     * @return статус 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Изменить роль пользователя.
     * @param id UUID пользователя
     * @param role новая роль
     * @return обновленный пользователь
     */
    @PatchMapping("/{id}/role")
    public ResponseEntity<AdminUserDTO> updateUserRole(@PathVariable UUID id, @RequestParam Role role) {
        return ResponseEntity.ok(adminService.updateUserRole(id, role));
    }

    /**
     * Заблокировать пользователя.
     * @param id UUID пользователя
     * @return обновленный пользователь с флагом "заблокирован"
     */
    @PatchMapping("/{id}/block")
    public ResponseEntity<AdminUserDTO> blockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.blockUser(id));
    }

    /**
     * Разблокировать пользователя.
     * @param id UUID пользователя
     * @return обновленный пользователь с флагом "разблокирован"
     */
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<AdminUserDTO> unblockUser(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.unblockUser(id));
    }
}
