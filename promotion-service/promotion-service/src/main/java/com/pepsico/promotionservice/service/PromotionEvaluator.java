package com.pepsico.promotionservice.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pepsico.promotionservice.dto.HcmEvent;
import com.pepsico.promotionservice.kafka.EmployeeProducer;
import com.pepsico.promotionservice.kafka.HcmEventConsumer;

@Service
public class PromotionEvaluator {
    private static final Logger log = LoggerFactory.getLogger(PromotionEvaluator.class);

	private final EmployeeProducer producer;

    public PromotionEvaluator(EmployeeProducer producer) {
        this.producer = producer;
    }

    public void evaluatePromotion(HcmEvent event) {
    	 if (event.getRoleJoinDate() == null || event.getDepartmentJoinDate() == null) {
    	        log.warn("Missing join dates for employeeId={}", event.getEmployeeId());
    	        producer.sendPromotionStatus(event.getEmployeeId(), false);
    	        return;
    	    }
        boolean eligibleForRole = ChronoUnit.YEARS.between(event.getRoleJoinDate(), LocalDate.now()) >= 3;
        boolean eligibleForDept = ChronoUnit.YEARS.between(event.getDepartmentJoinDate(), LocalDate.now()) >= 1;
        boolean goalsOrAppreciation = event.isGoalsCompleted() || event.isAppreciatedByClient();

        boolean isEligible = eligibleForRole && eligibleForDept && goalsOrAppreciation;
        log.info("Promotion eligibility: {} for employeeId={}", isEligible, event.getEmployeeId());

        producer.sendPromotionStatus(event.getEmployeeId(), isEligible);
    }
}
