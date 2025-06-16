package com.pepsico.hcmservice.kafka;

import org.springframework.stereotype.Service;

import com.pepsico.hcmservice.model.HcmRecord;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
@Service
public class KafkaProducerService {
	@Value("${kafka.topic.hcm-events}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishHcmEvent(String eventType, HcmRecord record) {
        Map<String, Object> message = Map.of(
            "eventType", eventType,
            "employeeId", record.getEmployeeId(),
            "experienceInYears", record.getYearsOfExperience(),
            "goalsCompleted", record.isGoalsCompleted(),
            "clientAppreciation", record.isAppreciatedByClient(),
            "isActive", record.isActive()
        );
        kafkaTemplate.send(topic, String.valueOf(record.getEmployeeId()), message);
    }
}
