package com.pepsico.hcmservice.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pepsico.hcmservice.kafka.KafkaProducerService;
import com.pepsico.hcmservice.model.HcmRecord;
import com.pepsico.hcmservice.repository.HcmRepository;

/**
 * Service for managing HCM (Human Capital Management) records and related events.
 * Handles creation, update, deactivation, and event publishing for HCM records.
 */
@Service
public class HcmService {
	private final HcmRepository repository;
    private final KafkaProducerService kafkaProducerService;

    public HcmService(HcmRepository repository, KafkaProducerService kafkaProducerService) {
        this.repository = repository;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Creates a new HCM record for a newly onboarded employee and publishes an event.
     * @param employeeId the employee's ID
     * @return the created HCM record
     */
    public HcmRecord createForNewEmployee(Long employeeId) {
        HcmRecord record = new HcmRecord();
        record.setEmployeeId(employeeId);
        record.setActive(true);
        record.setCreatedAt(LocalDate.now());
        record.setDepartmentJoinDate(LocalDate.now());
        record.setRoleJoinDate(LocalDate.now());
        HcmRecord saved = repository.save(record);

        // Publish creation event
        kafkaProducerService.publishHcmEvent("hcm.created", saved);

        return saved;
    }

    /**
     * Deactivates the HCM record for a given employee and publishes an event.
     * @param employeeId the employee's ID
     */
    public void deactivateHcmRecord(Long employeeId) {
        repository.findById(employeeId).ifPresent(record -> {
            record.setActive(false);
            HcmRecord saved = repository.save(record);

            // Publish deactivation event
            kafkaProducerService.publishHcmEvent("hcm.deactivated", saved);
        });
    }

    /**
     * Updates an HCM record and publishes an event.
     * @param record the HCM record to update
     * @return the updated HCM record
     */
    public HcmRecord updateRecord(HcmRecord record) {
        HcmRecord saved = repository.save(record);

        // Publish update event
        kafkaProducerService.publishHcmEvent("hcm.updated", saved);

        return saved;
    }

    /**
     * Retrieves the HCM record for a given employee ID.
     * @param id the employee's ID
     * @return the HCM record, or null if not found
     */
    public HcmRecord getByEmployeeId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
