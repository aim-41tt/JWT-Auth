package ru.example.JWT_Auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/test")
public class Test {

	@PostMapping
	public ResponseEntity<String> getMethodName() {
		System.out.println("Ok 200");
		return ResponseEntity.ok("всё ок");
	}

}
