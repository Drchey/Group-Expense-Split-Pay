package com.richey.groupsplitpay.controller;

import com.richey.groupsplitpay.dto.GroupMemberResponse;
import com.richey.groupsplitpay.security.SecurityUtils;
import com.richey.groupsplitpay.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group_member/")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupMemberService groupMemberService;
    private final SecurityUtils securityUtils;


    @GetMapping("/{groupId}/{userId}")
    public ResponseEntity<List<GroupMemberResponse>> getAllGroupMembers(@PathVariable Integer groupId, @PathVariable Integer userId){
        return ResponseEntity.ok(groupMemberService.getAllGroupMembers(groupId, userId
        ));
    }

    // Get A Group Member
    @GetMapping("/{groupId}/{userId}/{groupMemberId}")
    public  ResponseEntity<GroupMemberResponse> getGroupMemberById(@PathVariable Integer groupId, @PathVariable  Integer userId,@PathVariable Integer groupMemberId){
        return ResponseEntity.ok(groupMemberService.getGroupMemberById(groupId, userId, groupMemberId));
    }


    @PostMapping("/{groupId}/{newUserId}")
    public ResponseEntity<GroupMemberResponse> addGroupMemberToGroup(@PathVariable Integer groupId,@PathVariable Integer newUserId){
        Integer requestingUserId = securityUtils.getCurrentUserId();

        return ResponseEntity.status(HttpStatus.CREATED).body(groupMemberService.addGroupMemberToGroup(groupId, requestingUserId, newUserId));
    }

    @DeleteMapping("/{groupId}/{userId}")
    public ResponseEntity<Void> deleteGroupMemberById(@PathVariable Integer groupId, @PathVariable Integer userId){
        Integer requestingUserId = securityUtils.getCurrentUserId();
        groupMemberService.deleteGroupMemberById(groupId,requestingUserId, userId);
        return ResponseEntity.noContent().build();
    }


}
