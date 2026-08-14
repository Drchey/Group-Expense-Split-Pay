package com.richey.groupsplitpay.service;

import com.richey.groupsplitpay.dto.ExpenseRequest;
import com.richey.groupsplitpay.dto.SplitRequest;
import com.richey.groupsplitpay.dto.SplitResponse;
import com.richey.groupsplitpay.model.Expense;
import com.richey.groupsplitpay.model.Split;
import com.richey.groupsplitpay.model.User;
import com.richey.groupsplitpay.repo.ExpenseRepo;
import com.richey.groupsplitpay.repo.GroupMemberRepo;
import com.richey.groupsplitpay.repo.SplitRepo;
import com.richey.groupsplitpay.repo.UserRepo;
import com.richey.groupsplitpay.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SplitService {

    private final ExpenseRepo expenseRepo;
    private  final GroupMemberRepo groupMemberRepo;
    private final SplitRepo splitRepo;
    private final UserRepo userRepo;
    private  final SecurityUtils securityUtils;

    @Transactional
    public List<SplitResponse> getAllSplits(Integer expenseId){

        Integer currentUserId = securityUtils.getCurrentUserId();

        // Check if The Logged in user is a part d group where the expense is from
        Expense expense = expenseRepo.findById(expenseId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense Not Found")
        );

        Integer groupId = expense.getGroup().getId();

        groupMemberRepo.findByGroupIdAndUserId(groupId, currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this group")
        );

        return splitRepo.findAllByExpenseId(expenseId).stream()
                .map(
                        split -> new SplitResponse(
                                split.getId(),
                                split.getExpense().getId(),
                                split.getOwedByUser().getId(),
                                split.getCreatedByUser().getId(),
                                split.getAmount(),
                                split.isPaid(),
                                split.getCreatedAt()
                        )
                ).toList();
    }

    // Get A Single Split
    @Transactional
    public SplitResponse getSplitById(Integer splitId){
        Integer currentUserId = securityUtils.getCurrentUserId();

        Split split = splitRepo.findById(splitId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Split Not Found")
        );

        Integer groupId = split.getExpense().getGroup().getId();

        groupMemberRepo.findByGroupIdAndUserId(groupId, currentUserId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a member of this group")
        );

        return new SplitResponse(
                split.getId(),
                split.getExpense().getId(),
                split.getOwedByUser().getId(),
                split.getCreatedByUser().getId(),
                split.getAmount(),
                split.isPaid(),
                split.getCreatedAt()
        );
    }


    @Transactional
    public SplitResponse createSplit(SplitRequest request, Integer expenseId){
        Integer currentUserId = securityUtils.getCurrentUserId();

        Expense expense = expenseRepo.findById(expenseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        if (!expense.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the expense creator can create splits");
        }

        User owedByUser = userRepo.findById(request.owedByUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Integer groupId = expense.getGroup().getId();

        groupMemberRepo.findByGroupIdAndUserId(groupId, request.owedByUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a member of this group"));


        Split split = Split.builder()
                .expense(expense)
                .owedByUser(owedByUser)
                .createdByUser(expense.getUser())
                .amount(request.amount())
                .isPaid(false)
                .build();

        Split savedSplit = splitRepo.save(split);

        return new SplitResponse(
                savedSplit.getId(),
                savedSplit.getExpense().getId(),
                savedSplit.getOwedByUser().getId(),
                savedSplit.getCreatedByUser().getId(),
                savedSplit.getAmount(),
                savedSplit.isPaid(),
                savedSplit.getCreatedAt()
        );






    }
    // Delete Split
    @Transactional
    public void deleteSplit(Integer splitId) {

        Integer currentUserId = securityUtils.getCurrentUserId();

        Split split = splitRepo.findById(splitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Split not found"));

        if (!split.getCreatedByUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can delete this split");
        }

        splitRepo.delete(split);
    }

    @Transactional
    public SplitResponse updateSplit(Integer splitId, SplitRequest request) {

        Integer currentUserId = securityUtils.getCurrentUserId();

        Split split = splitRepo.findById(splitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Split not found"));

        if (!split.getCreatedByUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the creator can update this split");
        }

        User owedByUser = userRepo.findById(request.owedByUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Integer groupId = split.getExpense().getGroup().getId();
        groupMemberRepo.findByGroupIdAndUserId(groupId, request.owedByUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a member of this group"));

        split.setOwedByUser(owedByUser);
        split.setAmount(request.amount());

        Split savedSplit = splitRepo.save(split);

        return new SplitResponse(
                savedSplit.getId(),
                savedSplit.getExpense().getId(),
                savedSplit.getOwedByUser().getId(),
                savedSplit.getCreatedByUser().getId(),
                savedSplit.getAmount(),
                savedSplit.isPaid(),
                savedSplit.getCreatedAt()
        );
    }


    @Transactional
    public SplitResponse toggleSplitPaid(Integer splitId) {

        Integer currentUserId = securityUtils.getCurrentUserId();

        Split split = splitRepo.findById(splitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Split not found"));

        boolean isCreator = split.getCreatedByUser().getId().equals(currentUserId);
        boolean isDebtor = split.getOwedByUser().getId().equals(currentUserId);

        if (!isCreator && !isDebtor) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not involved in this split");
        }

        split.setPaid(!split.isPaid());

        Split savedSplit = splitRepo.save(split);

        return new SplitResponse(
                savedSplit.getId(),
                savedSplit.getExpense().getId(),
                savedSplit.getOwedByUser().getId(),
                savedSplit.getCreatedByUser().getId(),
                savedSplit.getAmount(),
                savedSplit.isPaid(),
                savedSplit.getCreatedAt()
        );
    }



}
