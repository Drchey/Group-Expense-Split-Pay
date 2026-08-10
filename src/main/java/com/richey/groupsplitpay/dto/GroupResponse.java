package com.richey.groupsplitpay.dto;

import java.time.LocalDateTime;

public record GroupResponse(
        Integer id,
        String name,
        String description,
        Integer user_id,
        LocalDateTime createdAt
) {
}
