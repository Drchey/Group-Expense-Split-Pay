package com.richey.groupsplitpay.controller;

import com.richey.groupsplitpay.dto.ExpenseRequest;
import com.richey.groupsplitpay.dto.ExpenseResponse;
import com.richey.groupsplitpay.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private  final ExpenseService expenseService;

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(@PathVariable Integer groupId){
        return ResponseEntity.ok(expenseService.getAllExpenses(groupId));
    }

    @PostMapping("/group/{groupId}")
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequest request, @PathVariable Integer groupId){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(request, groupId));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(@RequestBody ExpenseRequest request, @PathVariable Integer expenseId){
        return ResponseEntity.ok(expenseService.updateExpense(request, expenseId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Integer expenseId){
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.noContent().build();
    }

}
