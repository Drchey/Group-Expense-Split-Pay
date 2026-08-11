package com.richey.groupsplitpay.service;

import com.richey.groupsplitpay.dto.GroupMemberResponse;
import com.richey.groupsplitpay.model.Group;
import com.richey.groupsplitpay.model.GroupMember;
import com.richey.groupsplitpay.model.GroupRole;
import com.richey.groupsplitpay.model.User;
import com.richey.groupsplitpay.repo.GroupMemberRepo;
import com.richey.groupsplitpay.repo.GroupRepo;
import com.richey.groupsplitpay.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final GroupMemberRepo groupMemberRepo;
    private final GroupRepo groupRepo;
    private  final UserRepo userRepo;


    /**
     * - Fetch Group Member From a Group
     * - Fetch GroupMember By Id
     * - Add New Group Member
     * - Delete Group Member
     */

    public List<GroupMemberResponse> getAllGroupMembers(Integer groupId, Integer userId){
        // Check if Group id & Belongs to the current user
        boolean isMember = groupMemberRepo.existsByGroupIdAndUserId(groupId, userId);

        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not a member of this group");
        }

        // Get All Group Members in with the same group
        return groupMemberRepo.findAllByGroupId(groupId).stream()
                .map(groupMember -> new GroupMemberResponse(
                        groupMember.getId(),
                        groupMember.getUser().getId(),
                        groupMember.getGroup().getId(),
                        groupMember.getRole().name(),
                        groupMember.getCreatedAt()
                ))
                .toList();
    }



    public GroupMemberResponse getGroupMemberById(Integer groupId, Integer userId, Integer groupMemberId){
        boolean isMember = groupMemberRepo.existsByGroupIdAndUserId(groupId, userId);

        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not a member of this group");
        }

        GroupMember groupMember = groupMemberRepo.findById(groupMemberId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Member Not Found"));

        return  new GroupMemberResponse(
                groupMember.getId(),
                groupMember.getUser().getId(),
                groupMember.getGroup().getId(),
                groupMember.getRole().name(),
                groupMember.getCreatedAt()
        );
    }

     /**Add GroupMember */
     @Transactional
     public  GroupMemberResponse addGroupMemberToGroup(Integer groupId, Integer requestingUserId, Integer newUserId){
         GroupMember requesterMembership = groupMemberRepo.findByGroupIdAndUserId(groupId, requestingUserId)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not a member of this group"));

         // Check if Existing Member is admin
         if(requesterMembership.getRole() != GroupRole.ADMIN){
             throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not an admin");
         }

         // Check New User Exists
         User newUser = userRepo.findById(newUserId)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


         // Check if user already exists in group
         if (groupMemberRepo.existsByGroupIdAndUserId(groupId, newUserId)) {
             throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member of this group");
         }

         // Get the Group
         Group group = groupRepo.findById(groupId)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group not found"));

         GroupMember newMembership = GroupMember.builder()
                 .group(group)
                 .user(newUser)
                 .role(GroupRole.MEMBER)
                 .build();
         GroupMember saved = groupMemberRepo.save(newMembership);

         return new GroupMemberResponse(
                 saved.getId(),
                 saved.getUser().getId(),
                 saved.getGroup().getId(),
                 saved.getRole().name(),
                 saved.getCreatedAt()
         );

     }

    /** Remove GroupMember */

}
