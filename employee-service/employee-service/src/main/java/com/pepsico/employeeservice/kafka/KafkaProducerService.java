package com.pepsico.employeeservice.kafka;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.pepsico.employeeservice.model.Employee;

@Service
public class KafkaProducerService {
	@Value("${kafka.topic.employee-events}")
	private String topic;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void publishEmployeeEvent(String eventType, Employee employee) {
		Map<String, Object> event = Map.of("eventType", eventType, "employeeId", employee.getId(), "name",
				employee.getName(), "department", employee.getDepartment(), "departmentJoinDate",
				employee.getDepartmentJoinDate().toString(), "isActive", employee.isActive());
		kafkaTemplate.send(topic, String.valueOf(employee.getId()), event);
	}
}
