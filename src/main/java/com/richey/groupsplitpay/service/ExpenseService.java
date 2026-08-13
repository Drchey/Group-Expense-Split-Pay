package com.richey.groupsplitpay.service;

import com.richey.groupsplitpay.dto.ExpenseRequest;
import com.richey.groupsplitpay.dto.ExpenseResponse;
import com.richey.groupsplitpay.model.Expense;
import com.richey.groupsplitpay.model.Group;
import com.richey.groupsplitpay.model.GroupMember;
import com.richey.groupsplitpay.model.User;
import com.richey.groupsplitpay.repo.ExpenseRepo;
import com.richey.groupsplitpay.repo.GroupMemberRepo;
import com.richey.groupsplitpay.repo.GroupRepo;
import com.richey.groupsplitpay.security.SecurityConfig;
import com.richey.groupsplitpay.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final GroupMemberRepo groupMemberRepo;
    private final GroupRepo groupRepo;
    private final SecurityUtils securityUtils;
    private Expense expense;


    // get all expense in a group
    public List<ExpenseResponse> getAllExpenses(Integer groupId){
        Integer currentUserId = securityUtils.getCurrentUserId();
        // check if the user belong to the group
        Group group = groupRepo.findById(groupId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group Not Found")
        );

        // Check of the user is a member of the group
        GroupMember groupMember = groupMemberRepo.findByGroupIdAndUserId(group.getId(),currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not a Member of this group")
        );

        // Get all expenses with the group_id
        return expenseRepo.findAllByGroupId(group.getId()).stream().map(
                expense -> new ExpenseResponse(
                        expense.getTitle(),
                        expense.getCaption(),
                        expense.getTotalAmount(),
                        expense.getGroup(),
                        expense.getUser(),
                        expense.getCreatedAt()
                )
        ).toList();
    }

    /**
     * - Get the GroupId, Add a Expense 2 it
     *   Check if Logged In user is a member of the group
     *   - Add a Expense to the user and who
     * -
     * */

    public ExpenseResponse createExpense(ExpenseRequest request, Integer groupId){
        Integer currentUserId = securityUtils.getCurrentUserId();


        GroupMember groupMember = groupMemberRepo.findByGroupIdAndUserId(groupId,currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Unable to add to group")
        );

        Group group = groupMember.getGroup();
        User user = groupMember.getUser();

        Expense expense = Expense.builder()
                .title(request.title())
                .caption(request.caption())
                .totalAmount(request.totalAmount())
                .group(group)
                .user(user)
                .build();

        Expense savedExpense = expenseRepo.save(expense);

        return new ExpenseResponse(
                savedExpense.getTitle(),
                savedExpense.getCaption(),
                savedExpense.getTotalAmount(),
                savedExpense.getGroup(),
                savedExpense.getUser(),
                savedExpense.getCreatedAt()
        );

    }


    /**
     * Update My Expense . check if the User
     */
    public ExpenseResponse updateExpense(ExpenseRequest request, Integer expenseId){
        Integer currentUserId = securityUtils.getCurrentUserId();

        Expense expense = expenseRepo.findById(expenseId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense Not Found")
        );

        // Check if logged in user is the expense _user.id
        if(!currentUserId.equals(expense.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Permitted to Update this expense");

        }

        expense.setTitle(request.title());
        expense.setCaption(request.caption());
        expense.setTotalAmount(request.totalAmount());

        Expense updatedExpense = expenseRepo.save(expense);

        return  new ExpenseResponse(
                updatedExpense.getTitle(),
                updatedExpense.getCaption(),
                updatedExpense.getTotalAmount(),
                updatedExpense.getGroup(),
                updatedExpense.getUser(),
                updatedExpense.getCreatedAt()
        );
    }

    private void deleteExpense(Integer expenseId){
        Integer currentUserId = securityUtils.getCurrentUserId();

        Expense expense = expenseRepo.findById(expenseId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "expense not found")
        );

        if(!currentUserId.equals(expense.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not Permitted to Update this expense");
        }

        expenseRepo.deleteById(expense.getId());
    }

}
