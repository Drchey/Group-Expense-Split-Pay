package com.richey.groupsplitpay.dto;

public record GroupRequest(
        String name,
        String description,
        Integer user_id
) {
}
