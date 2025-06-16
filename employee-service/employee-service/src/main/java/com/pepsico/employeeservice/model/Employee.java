package com.pepsico.employeeservice.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private String department;
	private LocalDate departmentJoinDate;
	private boolean isActive = true;
	private boolean isEligibleForPromotion = false;

}