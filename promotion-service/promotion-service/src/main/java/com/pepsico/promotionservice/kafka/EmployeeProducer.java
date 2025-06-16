package com.pepsico.promotionservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
@Service
public class EmployeeProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.promotion-events}")
    private String topic;

    public EmployeeProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPromotionStatus(Long employeeId, boolean isEligible) {
        Map<String, Object> event = Map.of(
                "eventType", "PROMOTION_ELIGIBILITY_EVALUATED",
                "employeeId", employeeId,
                "isEligibleForPromotion", isEligible
        );
        kafkaTemplate.send(topic, employeeId.toString(), event);
    }
}
