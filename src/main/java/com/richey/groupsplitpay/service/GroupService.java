package com.richey.groupsplitpay.service;

import com.richey.groupsplitpay.dto.GroupRequest;
import com.richey.groupsplitpay.dto.GroupResponse;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepo groupRepo;
    private final UserRepo userRepo;
    private final GroupMemberRepo groupMemberRepo;
    /** Create Group */
    @Transactional
    public GroupResponse createGroup(GroupRequest request, Integer userId){
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (groupRepo.existsByUserIdAndName(userId, request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Group with this name already exists");
        }

        Group group = Group.builder()
                           .name(request.name())
                          .description(request.description())
                           .user(user).build();

        Group savedGroup = groupRepo.save(group);

        GroupMember newAdmin = GroupMember.builder().group(savedGroup).user(user).role(GroupRole.ADMIN).build();
        groupMemberRepo.save(newAdmin);


        return new GroupResponse(
                savedGroup.getId(),
                savedGroup.getName(),
                savedGroup.getDescription(),
                savedGroup.getUser().getId(),
                savedGroup.getCreatedAt()
        );
    }


    /** List All Groups from User*/
    public List<GroupResponse> listGroups(Integer userId){
        return groupRepo.findAllByUserId(userId)
                .stream().map(
                        group -> new GroupResponse(
                                group.getId(),
                                group.getName(),
                                group.getDescription(),
                                group.getUser().getId(),
                                group.getCreatedAt()
                        )
                ).toList();
    }

    /** Get A Specific Group from UserId */
    public GroupResponse listGroupById(Integer groupId, Integer userId){
        Group group =  groupRepo.findByIdAndUserId(groupId, userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found"));

        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                group.getUser().getId(),
                group.getCreatedAt()
        );
    }

    /** Update Group */
    public GroupResponse updateGroupById(GroupRequest request, Integer groupId, Integer userId){
        Group group =  groupRepo.findByIdAndUserId(groupId, userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found"));

        // Check if its Unique
        if (groupRepo.existsByUserIdAndName(userId, request.name())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Group with this name already exists");
        }

        group.setName(request.name());
        group.setDescription(request.description());

        Group updatedGroup = groupRepo.save(group);

        return new GroupResponse(
                updatedGroup.getId(),
                updatedGroup.getName(),
                updatedGroup.getDescription(),
                updatedGroup.getUser().getId(),
                updatedGroup.getCreatedAt()
        );


    }



    /** Delete Group */
    public void deleteGroupByOwner(Integer groupId, Integer userId){

        Group group =  groupRepo.findByIdAndUserId(groupId, userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found"));

        groupRepo.deleteById(group.getId());

    }
}
