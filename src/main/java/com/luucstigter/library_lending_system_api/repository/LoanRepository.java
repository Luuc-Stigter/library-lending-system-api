package com.luucstigter.library_lending_system_api.repository;

import com.luucstigter.library_lending_system_api.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}