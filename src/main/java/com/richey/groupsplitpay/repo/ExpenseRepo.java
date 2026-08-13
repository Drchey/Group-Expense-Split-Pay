package com.richey.groupsplitpay.repo;

import com.richey.groupsplitpay.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, Integer> {
    List<Expense> findAllByGroupId(Integer groupId);
}
