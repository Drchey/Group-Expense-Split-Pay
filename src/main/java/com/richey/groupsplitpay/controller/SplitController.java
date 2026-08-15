package com.richey.groupsplitpay.controller;

import com.richey.groupsplitpay.dto.SplitRequest;
import com.richey.groupsplitpay.dto.SplitResponse;
import com.richey.groupsplitpay.repo.SplitRepo;
import com.richey.groupsplitpay.service.SplitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/splits")
@RequiredArgsConstructor
public class SplitController {

    private final SplitService splitService;

    @GetMapping("/get_all/{expenseId}")
    public ResponseEntity<List<SplitResponse>> getAllSplits(@PathVariable  Integer expenseId){
        return ResponseEntity.ok(splitService.getAllSplits(expenseId));
    }

    @GetMapping("/{splitId}")
    public ResponseEntity<SplitResponse> getSplitById(@PathVariable Integer splitId){
        return ResponseEntity.ok(splitService.getSplitById(splitId));
    }

    @PostMapping("/create/{expenseId}")
    public ResponseEntity<SplitResponse> createSplit(@RequestBody SplitRequest request, @PathVariable Integer expenseId){
        return ResponseEntity.status(HttpStatus.CREATED).body(splitService.createSplit(request, expenseId));
    }


    @DeleteMapping("/{splitId}")
    public ResponseEntity<Void> deleteSplit(@PathVariable Integer splitId){
        splitService.deleteSplit(splitId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{splitId}")
    public ResponseEntity<SplitResponse> updateSplit(@PathVariable Integer splitId, @RequestBody SplitRequest request){
        return ResponseEntity.ok(splitService.updateSplit(splitId, request));
    }

    @PutMapping("/toogle/{splitId}")
    public ResponseEntity<SplitResponse> toogleSplitPaid(@PathVariable Integer splitId){
        return ResponseEntity.ok(splitService.toggleSplitPaid(splitId));
    }


}
