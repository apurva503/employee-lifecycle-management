package com.pepsico.hcmservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepsico.hcmservice.model.HcmRecord;
import com.pepsico.hcmservice.service.HcmService;

@RestController
@RequestMapping("/hcm")
public class HcmController {
	private final HcmService hcmService;
    public HcmController(HcmService hcmService) {
        this.hcmService = hcmService;
    }

    @GetMapping("/{employeeId}")
    public HcmRecord getByEmployeeId(@PathVariable Long employeeId) {
        return hcmService.getByEmployeeId(employeeId);
    }

    @PutMapping
    public HcmRecord update(@RequestBody HcmRecord record) {
        return hcmService.updateRecord(record);
    }
}
