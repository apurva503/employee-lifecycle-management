package com.pepsico.hcmservice.kafka;

import org.springframework.stereotype.Service;

import com.pepsico.hcmservice.model.HcmRecord;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.retry.annotation.Retryable;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.retry.annotation.Backoff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class KafkaProducerService {
	private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

	@Value("${kafka.topic.hcm-events}")
    private String topic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    @CircuitBreaker(name = "hcmKafkaProducerCB", fallbackMethod = "handleKafkaFailure")
    public void publishHcmEvent(String eventType, HcmRecord record) {
    	 DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    	 Map<String, Object> message = new HashMap<>();
    	 message.put("eventType", eventType);
    	 message.put("employeeId", record.getEmployeeId());
    	 message.put("experienceInYears", record.getYearsOfExperience());
    	 message.put("goalsCompleted", record.isGoalsCompleted());
    	 message.put("clientAppreciation", record.isAppreciatedByClient());
    	 message.put("isActive", record.isActive());
    	 message.put("departmentJoinDate", record.getDepartmentJoinDate() != null ? record.getDepartmentJoinDate().format(formatter) : null);
         message.put("roleJoinDate", record.getRoleJoinDate() != null ? record.getRoleJoinDate().format(formatter) : null);

        kafkaTemplate.send(topic, String.valueOf(record.getEmployeeId()), message);
    }
    public void handleKafkaFailure(String eventType, HcmRecord record, Throwable throwable) {
        log.error("Kafka publish failed for employeeId={} in HCM service. Error: {}", record.getEmployeeId(), throwable);
    }
}
