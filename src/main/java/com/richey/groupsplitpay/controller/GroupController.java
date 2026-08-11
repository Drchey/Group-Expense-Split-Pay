package com.richey.groupsplitpay.controller;

import com.richey.groupsplitpay.dto.GroupRequest;
import com.richey.groupsplitpay.dto.GroupResponse;
import com.richey.groupsplitpay.model.Group;
import com.richey.groupsplitpay.model.User;
import com.richey.groupsplitpay.security.SecurityUtils;
import com.richey.groupsplitpay.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final SecurityUtils securityUtils;

    @GetMapping("")
    public ResponseEntity<List<GroupResponse>> listGroups(){
        Integer userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(groupService.listGroups(userId));
    }

    @PostMapping("")
    public ResponseEntity<GroupResponse> createGroup(
            @Valid @RequestBody GroupRequest request) {
        Integer userId = securityUtils.getCurrentUserId();
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(groupService.createGroup(request, userId));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> listGroupById(@PathVariable Integer groupId){
        Integer userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(groupService.listGroupById(groupId, userId));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupResponse> updateGroupById(@RequestBody GroupRequest request, @PathVariable Integer groupId){
        Integer userId = securityUtils.getCurrentUserId();
        return ResponseEntity.ok(groupService.updateGroupById(request, groupId, userId));
    }


    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupById(Integer groupId){
        Integer userId = securityUtils.getCurrentUserId();
        groupService.deleteGroupByOwner(groupId, userId);
        return ResponseEntity.noContent().build();
    }


}
