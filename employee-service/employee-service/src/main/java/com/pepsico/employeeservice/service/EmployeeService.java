package com.pepsico.employeeservice.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pepsico.employeeservice.dto.EmployeeDto;
import com.pepsico.employeeservice.kafka.KafkaProducerService;
import com.pepsico.employeeservice.model.Employee;
import com.pepsico.employeeservice.repository.EmployeeRepository;

@Service
public class EmployeeService {
	private final EmployeeRepository repo;
	private final KafkaProducerService kafkaProducer;

	public EmployeeService(EmployeeRepository repo, KafkaProducerService kafkaProducer) {
		this.repo = repo;
		this.kafkaProducer = kafkaProducer;
	}

	public Employee createEmployee(EmployeeDto dto) {
		Employee e = new Employee();
		e.setName(dto.name());
		e.setDepartment(dto.department());
		e.setDepartmentJoinDate(LocalDate.now());
		e.setActive(true);
		Employee saved = repo.save(e);
		kafkaProducer.publishEmployeeEvent("employee.created", saved);
		return saved;
	}

	public Employee deactivateEmployee(Long id) {
		Employee e = repo.findById(id).orElseThrow();
		e.setActive(false);
		Employee saved = repo.save(e);
		kafkaProducer.publishEmployeeEvent("employee.deactivated", saved);
		return saved;
	}

	public List<Employee> getAllEmployees() {
		return repo.findAll();
	}

}
