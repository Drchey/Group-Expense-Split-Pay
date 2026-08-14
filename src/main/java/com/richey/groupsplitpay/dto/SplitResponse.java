package com.richey.groupsplitpay.dto;

import com.richey.groupsplitpay.model.Expense;
import com.richey.groupsplitpay.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SplitResponse(
        Integer id,
        Integer expenseId,
        Integer owedByUserId,
        Integer createdByUserId,
        BigDecimal amount,
        boolean isPaid,
        LocalDateTime createdAt
) {
}
