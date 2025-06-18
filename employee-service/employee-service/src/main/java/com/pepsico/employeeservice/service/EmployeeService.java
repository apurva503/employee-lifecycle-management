package com.pepsico.employeeservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pepsico.employeeservice.dto.EmployeeDto;
import com.pepsico.employeeservice.kafka.KafkaProducerService;
import com.pepsico.employeeservice.model.Employee;
import com.pepsico.employeeservice.repository.EmployeeRepository;
import com.pepsico.employeeservice.exception.ResourceNotFoundException;

@Service
public class EmployeeService {
	private final EmployeeRepository repo;
	private final KafkaProducerService kafkaProducer;

	public EmployeeService(EmployeeRepository repo, KafkaProducerService kafkaProducer) {
		this.repo = repo;
		this.kafkaProducer = kafkaProducer;
	}

	/**
	 * Creates a new employee and publishes an event.
	 * @param dto Employee data
	 * @return the created Employee
	 */
	public Employee createEmployee(EmployeeDto dto) {
		Employee e = new Employee();
		e.setName(dto.name());
		e.setDepartment(dto.department());
		//e.setDepartmentJoinDate(LocalDate.now());
		e.setDepartmentJoinDate(LocalDate.now().minusYears(5)); // For testing purposes, set to 1 year ago
		e.setActive(true);
		Employee saved = repo.save(e);
		kafkaProducer.publishEmployeeEvent("employee.created", saved);
		return saved;
	}

	/**
	 * Deactivates an employee by ID and publishes an event.
	 * @param id Employee ID
	 * @return the updated Employee
	 * @throws ResourceNotFoundException if employee is not found
	 */
	public Employee deactivateEmployee(Long id) {
		Employee e = repo.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
		e.setActive(false);
		Employee saved = repo.save(e);
		kafkaProducer.publishEmployeeEvent("employee.deactivated", saved);
		return saved;
	}

	/**
	 * Returns all employees.
	 * @return list of employees
	 */
	public List<Employee> getAllEmployees() {
		return repo.findAll();
	}

}
