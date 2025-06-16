package com.pepsico.promotionservice.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HcmEvent {
	 private Long employeeId;
	    private String eventType; // e.g. "HCM_UPDATED"
	    private boolean goalsCompleted;
	    private boolean appreciatedByClient;
	    private LocalDate departmentJoinDate;
	    private LocalDate roleJoinDate;
}
