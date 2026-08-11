package com.richey.groupsplitpay.dto;

public record GroupMemberRequest(
        Integer user_id,
        Integer group_id,
        String role
) {
}
