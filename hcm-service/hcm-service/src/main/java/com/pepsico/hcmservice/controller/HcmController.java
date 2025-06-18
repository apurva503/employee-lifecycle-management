package com.pepsico.hcmservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepsico.hcmservice.model.HcmRecord;
import com.pepsico.hcmservice.service.HcmService;

/**
 * REST controller for managing HCM (Human Capital Management) records.
 * Provides endpoints to retrieve and update HCM records for employees.
 */
@RestController
@RequestMapping("/hcm")
public class HcmController {
	private final HcmService hcmService;
    public HcmController(HcmService hcmService) {
        this.hcmService = hcmService;
    }

    /**
     * Retrieves the HCM record for a given employee ID.
     * @param employeeId the employee's ID
     * @return the HCM record for the employee
     */
    @GetMapping("/{employeeId}")
    public HcmRecord getByEmployeeId(@PathVariable Long employeeId) {
        return hcmService.getByEmployeeId(employeeId);
    }

    /**
     * Updates an HCM record.
     * @param record the HCM record to update
     * @return the updated HCM record
     */
    @PutMapping
    public HcmRecord update(@RequestBody HcmRecord record) {
        return hcmService.updateRecord(record);
    }
}
