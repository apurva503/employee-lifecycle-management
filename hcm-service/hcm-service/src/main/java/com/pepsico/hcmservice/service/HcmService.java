package com.pepsico.hcmservice.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pepsico.hcmservice.kafka.KafkaProducerService;
import com.pepsico.hcmservice.model.HcmRecord;
import com.pepsico.hcmservice.repository.HcmRepository;

@Service
public class HcmService {
	private final HcmRepository repository;
    private final KafkaProducerService kafkaProducerService;

    public HcmService(HcmRepository repository, KafkaProducerService kafkaProducerService) {
        this.repository = repository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public HcmRecord createForNewEmployee(Long employeeId) {
        HcmRecord record = new HcmRecord();
        record.setEmployeeId(employeeId);
        record.setActive(true);
        record.setCreatedAt(LocalDate.now());

        HcmRecord saved = repository.save(record);

        // 🔴 Publish creation event
        kafkaProducerService.publishHcmEvent("hcm.created", saved);

        return saved;
    }

    public void deactivateHcmRecord(Long employeeId) {
        repository.findById(employeeId).ifPresent(record -> {
            record.setActive(false);
            HcmRecord saved = repository.save(record);

            // 🔴 Publish deactivation event
            kafkaProducerService.publishHcmEvent("hcm.deactivated", saved);
        });
    }

    public HcmRecord updateRecord(HcmRecord record) {
        HcmRecord saved = repository.save(record);

        // 🔴 Publish update event
        kafkaProducerService.publishHcmEvent("hcm.updated", saved);

        return saved;
    }

    public HcmRecord getByEmployeeId(Long id) {
        return repository.findById(id).orElse(null);
    }
}
