package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/test")
public class Test {

	@GetMapping
	
	public ResponseEntity<String> getMethodName() {
		return ResponseEntity.ok("всё ок");
	}
	
	
}
