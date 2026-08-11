package com.richey.groupsplitpay.repo;

import com.richey.groupsplitpay.model.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMemberRepo extends JpaRepository<GroupMember, Integer> {

    boolean existsByGroupIdAndUserId(Integer group_id, Integer user_id);
    Optional<GroupMember> findByGroupIdAndUserId(Integer groupId, Integer userId);
    List<GroupMember> findAllByGroupId(Integer group_id);

}
