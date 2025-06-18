package com.luucstigter.library_lending_system_api.controller;

import com.luucstigter.library_lending_system_api.dto.LoanDto;
import com.luucstigter.library_lending_system_api.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/item/{itemId}")
    public ResponseEntity<LoanDto> createLoan(@PathVariable Long itemId, Principal principal) {
        LoanDto newLoanDto = loanService.createLoan(itemId, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLoanDto);
    }
}