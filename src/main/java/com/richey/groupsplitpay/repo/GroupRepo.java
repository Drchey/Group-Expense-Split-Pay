package com.richey.groupsplitpay.repo;

import com.richey.groupsplitpay.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepo extends JpaRepository<Group, Integer> {

    boolean existsByUserIdAndName(Integer user_id, String name);

    List<Group> findAllByUserId(Integer user_id);

    Optional<Group> findByIdAndUserId(Integer id, Integer user_id);
}
