package com.pepsico.employeeservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepsico.employeeservice.dto.EmployeeDto;
import com.pepsico.employeeservice.model.Employee;
import com.pepsico.employeeservice.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody @Valid EmployeeDto dto) {
        return ResponseEntity.ok(service.createEmployee(dto));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Employee> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(service.deactivateEmployee(id));
    }

    @GetMapping
    public ResponseEntity<List<Employee>> all() {
        return ResponseEntity.ok(service.getAllEmployees());
    }
}
