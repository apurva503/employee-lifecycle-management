package com.pepsico.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pepsico.employeeservice.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
