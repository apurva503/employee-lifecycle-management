package com.pepsico.employeeservice.kafka;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.pepsico.employeeservice.model.Employee;
import com.pepsico.employeeservice.repository.EmployeeRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class KafkaProducerService {
	private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

	@Value("${kafka.topic.employee-events}")
	private String topic;

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	@CircuitBreaker(name = "kafkaProducerCB", fallbackMethod = "handleKafkaFailure")
	public void publishEmployeeEvent(String eventType, Employee employee) {
		Map<String, Object> event = Map.of("eventType", eventType, "employeeId", employee.getId(), "name",
				employee.getName(), "department", employee.getDepartment(), "departmentJoinDate",
				employee.getDepartmentJoinDate().toString(), "isActive", employee.isActive());
		kafkaTemplate.send(topic, String.valueOf(employee.getId()), event);
	}
	// Fallback method
    public void handleKafkaFailure(String eventType, Employee employee, Throwable throwable) {
    	log.error("Kafka publish failed for employeeId={} after retries. Error: {}", employee.getId(), throwable.getMessage());
        //  store to DB for manual retry later
    	employee.setKafkaSendFailed(true);
    	employeeRepository.save(employee);
    	log.info("Employee event saved to DB for manual retry later: {}", employee);
		
    }
   
    @Scheduled(fixedDelay = 60000)
    public void retryFailedKafkaPublishes() {
        List<Employee> failedEmployees = employeeRepository.findByKafkaSendFailedTrue();

        for (Employee emp : failedEmployees) {
            try {
                publishEmployeeEvent("RETRY_EVENT", emp);
                emp.setKafkaSendFailed(false);
            } catch (Exception e) {
				log.error("Retry failed for employeeId={} with error: {}", emp.getId(), e.getMessage());
            }
            employeeRepository.save(emp);
        }
    }
}
