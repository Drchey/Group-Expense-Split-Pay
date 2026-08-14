package com.richey.groupsplitpay.dto;

import com.richey.groupsplitpay.model.Expense;
import com.richey.groupsplitpay.model.User;

import java.math.BigDecimal;

public record SplitRequest(
        Integer expenseId,
        Integer owedByUserId,
        BigDecimal amount
) {
}
