package com.richey.groupsplitpay.dto;

import com.richey.groupsplitpay.model.Group;
import com.richey.groupsplitpay.model.User;

import java.math.BigDecimal;

public record ExpenseRequest(
        String title,
        String caption,
        BigDecimal totalAmount,
        Group group,
        User user
) {
}
