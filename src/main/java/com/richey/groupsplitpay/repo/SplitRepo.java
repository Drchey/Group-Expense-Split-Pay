package com.richey.groupsplitpay.repo;

import com.richey.groupsplitpay.model.Split;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SplitRepo extends JpaRepository<Split, Integer> {

    List<Split> findAllByExpenseId(Integer expenseId);

}
