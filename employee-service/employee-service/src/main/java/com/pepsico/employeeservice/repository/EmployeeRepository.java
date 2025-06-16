package com.pepsico.employeeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepsico.employeeservice.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByKafkaSendFailedTrue();
}
