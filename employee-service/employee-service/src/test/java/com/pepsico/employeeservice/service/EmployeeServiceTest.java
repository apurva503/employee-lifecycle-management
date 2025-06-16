package com.pepsico.employeeservice.service;

import com.pepsico.employeeservice.dto.EmployeeDto;
import com.pepsico.employeeservice.kafka.KafkaProducerService;
import com.pepsico.employeeservice.model.Employee;
import com.pepsico.employeeservice.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

	@Mock
	private EmployeeRepository employeeRepository;

	@Mock
	private KafkaProducerService kafkaProducerService;

	@InjectMocks
	private EmployeeService employeeService;

	@Test
	void createEmployee_shouldSaveEmployeeAndPublishEvent() {
		// Arrange
		EmployeeDto dto = new EmployeeDto("Test user", "Test dept");

		Employee savedEmployee = new Employee();
		savedEmployee.setId(500L);
		savedEmployee.setName(dto.name());
		savedEmployee.setDepartment(dto.department());
		savedEmployee.setDepartmentJoinDate(LocalDate.now());
		savedEmployee.setActive(true);

		when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);

		// Act
		Employee result = employeeService.createEmployee(dto);

		// Assert
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo(dto.name());
		assertThat(result.getDepartment()).isEqualTo(dto.department());
		assertThat(result.isActive()).isTrue();

		verify(employeeRepository).save(any(Employee.class));
		verify(kafkaProducerService).publishEmployeeEvent(eq("employee.created"), any(Employee.class));

	}

	@Test
	void deactivateEmployee_shouldSetInactiveAndPublishEvent() {
		// Arrange
		Long employeeId = 1L;
		Employee employee = new Employee();
		employee.setId(employeeId);
		employee.setName("John");
		employee.setDepartment("ERS");
		employee.setActive(true);
		employee.setDepartmentJoinDate(LocalDate.now());

		when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
		when(employeeRepository.save(any())).thenReturn(employee);

		// Act
		Employee result = employeeService.deactivateEmployee(employeeId);

		// Assert
		assertThat(result.isActive()).isFalse();
		verify(employeeRepository).save(employee);
		verify(kafkaProducerService).publishEmployeeEvent(eq("employee.deactivated"), eq(employee));
	}

	@Test
	void getAllEmployees_shouldReturnListOfEmployees() {
		// Arrange
		Employee e1 = new Employee();
		e1.setId(1L);
		e1.setName("Alice");
		e1.setDepartment("Tech");

		Employee e2 = new Employee();
		e2.setId(2L);
		e2.setName("Bob");
		e2.setDepartment("HR");

		List<Employee> mockList = List.of(e1, e2);

		when(employeeRepository.findAll()).thenReturn(mockList);

		// Act
		List<Employee> result = employeeService.getAllEmployees();

		// Assert
		assertThat(result).hasSize(2);
		assertThat(result).extracting(Employee::getName).containsExactly("Alice", "Bob");

		verify(employeeRepository).findAll();
	}
}
