package com.richey.groupsplitpay.dto;

import com.richey.groupsplitpay.model.Group;
import com.richey.groupsplitpay.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(
        String title,
        String caption,
        BigDecimal totalAmount,
        Group group,
        User user,
        LocalDateTime createdAt

) {
}
