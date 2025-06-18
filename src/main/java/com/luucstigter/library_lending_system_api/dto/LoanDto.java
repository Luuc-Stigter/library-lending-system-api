package com.luucstigter.library_lending_system_api.dto;

import java.time.LocalDate;

public class LoanDto {
    public Long id;
    public Long itemId;
    public String bookTitle;
    public String username;
    public LocalDate loanDate;
    public LocalDate dueDate;
    public String status;
}