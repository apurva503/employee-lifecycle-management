package com.pepsico.promotionservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.pepsico.promotionservice.dto.HcmEvent;
import com.pepsico.promotionservice.service.PromotionEvaluator;

@Service
public class HcmEventConsumer {
	private final PromotionEvaluator evaluator;
    private static final Logger log = LoggerFactory.getLogger(HcmEventConsumer.class);

    public HcmEventConsumer(PromotionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @KafkaListener(topics = "${kafka.topic.hcm-events}", groupId = "promotion-service-group")
    public void consume(HcmEvent event) {
        log.info("Received HCM Event: {}", event);
        evaluator.evaluatePromotion(event);
    }
}
