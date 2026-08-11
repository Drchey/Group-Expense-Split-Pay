package com.richey.groupsplitpay.dto;

import java.time.LocalDateTime;

public record GroupMemberResponse(
        Integer id,
        Integer user_id,
        Integer group_id,
        String role,
        LocalDateTime createdAt
) {
}
