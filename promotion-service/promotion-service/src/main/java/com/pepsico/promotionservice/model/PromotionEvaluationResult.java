package com.pepsico.promotionservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionEvaluationResult {
	private Long employeeId;
    private boolean eligible;
}
