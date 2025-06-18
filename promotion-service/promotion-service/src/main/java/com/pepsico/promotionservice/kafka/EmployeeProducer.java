package com.pepsico.promotionservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Kafka producer service for publishing promotion evaluation results.
 * Publishes promotion eligibility events to Kafka for employees.
 */
@Service
public class EmployeeProducer {
	private static final Logger log = LoggerFactory.getLogger(EmployeeProducer.class);

	private final KafkaTemplate<String, Object> kafkaTemplate;

	@Value("${kafka.topic.promotion-events}")
	private String topic;

	public EmployeeProducer(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	/**
	 * Publishes a promotion eligibility event to Kafka for a given employee.
	 * @param employeeId the employee's ID
	 * @param isEligible whether the employee is eligible for promotion
	 */
	@Retryable(value = { Exception.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	@CircuitBreaker(name = "promotionKafkaProducerCB", fallbackMethod = "fallbackSend")
	public void sendPromotionStatus(Long employeeId, boolean isEligible) {
		Map<String, Object> event = Map.of("eventType", "PROMOTION_ELIGIBILITY_EVALUATED", "employeeId", employeeId,
				"isEligibleForPromotion", isEligible);
		kafkaTemplate.send(topic, employeeId.toString(), event);
	}
	
	/**
	 * Fallback method invoked when Kafka publishing fails after retries and circuit breaker.
	 * @param employeeId the employee's ID
	 * @param isEligible promotion eligibility
	 * @param throwable the exception thrown during publishing
	 */
	public void fallbackSend(Long employeeId, boolean isEligible, Throwable throwable) {
		log.error("Failed to send promotion event after retries. Storing for manual retry. Error: {}", throwable.getMessage());
	}
}
