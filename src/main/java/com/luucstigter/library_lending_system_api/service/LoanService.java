package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.dto.LoanDto;
import com.luucstigter.library_lending_system_api.exception.ResourceConflictException;
import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.model.Loan;
import com.luucstigter.library_lending_system_api.model.User;
import com.luucstigter.library_lending_system_api.repository.ItemRepository;
import com.luucstigter.library_lending_system_api.repository.LoanRepository;
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public LoanService(LoanRepository loanRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public LoanDto createLoan(Long itemId, Principal principal) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item niet gevonden met id: " + itemId));

        if (!"AVAILABLE".equalsIgnoreCase(item.getStatus())) {
            throw new ResourceConflictException("Item is niet beschikbaar voor uitleen. Huidige status: " + item.getStatus());
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("gebruiker '" + principal.getName() + "' niet gevonden."));

        Loan loan = new Loan();
        loan.setItem(item);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusWeeks(3));
        loan.setStatus("ACTIVE");

        item.setStatus("ON_LOAN");
        itemRepository.save(item);

        Loan newLoan = loanRepository.save(loan);
        return convertLoanToDto(newLoan);
    }

    private LoanDto convertLoanToDto(Loan loan) {
        LoanDto dto = new LoanDto();
        dto.id = loan.getId();
        dto.itemId = loan.getItem().getId();
        dto.bookTitle = loan.getItem().getBook().getTitle();
        dto.username = loan.getUser().getUsername();
        dto.loanDate = loan.getLoanDate();
        dto.dueDate = loan.getDueDate();
        dto.status = loan.getStatus();
        return dto;
    }
}