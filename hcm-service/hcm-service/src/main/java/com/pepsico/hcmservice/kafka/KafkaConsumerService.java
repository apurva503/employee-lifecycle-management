package com.pepsico.hcmservice.kafka;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pepsico.hcmservice.service.HcmService;

/**
 * Kafka consumer service for handling employee-related events.
 * Listens to employee events and triggers HCM record creation or deactivation.
 */
@Service
public class KafkaConsumerService {
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

	private final HcmService hcmService;

	public KafkaConsumerService(HcmService hcmService) {
		this.hcmService = hcmService;
	}

	/**
	 * Handles employee events received from Kafka and triggers appropriate HCM actions.
	 * @param message the event message from Kafka
	 */
	@KafkaListener(topics = "${kafka.topic.employee-events}", groupId = "hcm-group")
	public void handleEmployeeEvents(Map<String, Object> message) {
		log.info("Received employee event: {}", message);
		String eventType = (String) message.get("eventType");
		Long employeeId = Long.valueOf(message.get("employeeId").toString());

		if ("employee.created".equalsIgnoreCase(eventType)) {
			hcmService.createForNewEmployee(employeeId);
		} else if ("employee.deactivated".equalsIgnoreCase(eventType)) {
			hcmService.deactivateHcmRecord(employeeId);
		}
	}
}
