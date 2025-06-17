package com.pepsico.hcmservice.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HcmRecord {
	@Id
	private Long employeeId;

	private int yearsOfExperience;
	private boolean goalsCompleted;
	private boolean appreciatedByClient;
	private boolean active;

	private LocalDate createdAt;
	private LocalDate departmentJoinDate;
	private LocalDate roleJoinDate;

}
