package com.luucstigter.library_lending_system_api.config;

import com.luucstigter.library_lending_system_api.model.*; // Gebruik wildcard voor eenvoud
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import com.luucstigter.library_lending_system_api.repository.LoanRepository; // NIEUW
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, BookRepository bookRepository, LoanRepository loanRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0 && bookRepository.count() == 0) {
            System.out.println("Database is leeg. Bezig met vullen van testdata...");
            User member = seedMember();
            seedLibrarian();
            seedBooksAndLoans(member);
            System.out.println("Testdata succesvol toegevoegd.");
        } else {
            System.out.println("Database bevat al data. Seeder wordt overgeslagen.");
        }
    }

    private User seedMember() {
        User member = new User();
        member.setUsername("member");
        member.setPassword(passwordEncoder.encode("password"));
        member.setEmail("member@library.nl");
        member.setRole("MEMBER");
        member.setMembershipStartDate(LocalDate.parse("2024-01-15"));
        return userRepository.save(member);
    }

    private void seedLibrarian() {
        User librarian = new User();
        librarian.setUsername("librarian");
        librarian.setPassword(passwordEncoder.encode("password"));
        librarian.setEmail("librarian@library.nl");
        librarian.setRole("LIBRARIAN");
        librarian.setMembershipStartDate(LocalDate.parse("2023-05-20"));
        userRepository.save(librarian);
    }

    private void seedBooksAndLoans(User member) {
        // --- Boek 1: Dune ---
        Book dune = new Book();
        dune.setTitle("Dune");
        dune.setAuthor("Frank Herbert");
        dune.setIsbn("978-0441013593");
        dune.setPublisher("Ace Books");
        dune.setPublicationYear(1965);
        dune.setGenre("Science Fiction");

        Item duneHardcover = new Item();
        duneHardcover.setType("HARDCOVER");
        duneHardcover.setStatus("AVAILABLE");
        duneHardcover.setBook(dune);

        Item duneEbook = new Item();
        duneEbook.setType("EBOOK_PDF");
        duneEbook.setStatus("AVAILABLE");
        duneEbook.setBook(dune);

        // Koppel een dummy digitaal bestand aan het ebook
        DigitalBookFile duneFile = new DigitalBookFile();
        duneFile.setOriginalFileName("dune_herbert.pdf");
        duneFile.setStoredFileName("dummy_dune_file.pdf");
        duneFile.setFileType("application/pdf");
        duneFile.setSize(1500000L);
        duneFile.setUploadDate(LocalDate.now());
        duneFile.setItem(duneEbook); // Koppel bestand aan item
        duneEbook.setDigitalBookFile(duneFile); // Koppel item aan bestand

        dune.setItems(Set.of(duneHardcover, duneEbook));
        bookRepository.save(dune);

        // --- Boek 2: The Lord of the Rings ---
        Book lotr = new Book();
        lotr.setTitle("The Lord of the Rings");
        lotr.setAuthor("J.R.R. Tolkien");
        lotr.setIsbn("978-0618640157");
        lotr.setPublisher("Mariner Books");
        lotr.setPublicationYear(1954);
        lotr.setGenre("Fantasy");

        Item lotrPaperback = new Item();
        lotrPaperback.setType("PAPERBACK");
        lotrPaperback.setStatus("ON_LOAN");
        lotrPaperback.setBook(lotr);

        lotr.setItems(Set.of(lotrPaperback));
        bookRepository.save(lotr); // Sla eerst boek en item op om IDs te genereren

        // Maak nu de bijbehorende LENING aan die de ON_LOAN status verklaart
        Loan lotrLoan = new Loan();
        lotrLoan.setItem(lotrPaperback);
        lotrLoan.setUser(member);
        lotrLoan.setLoanDate(LocalDate.now().minusDays(10));
        lotrLoan.setDueDate(LocalDate.now().plusDays(11));
        lotrLoan.setStatus("ACTIVE");
        loanRepository.save(lotrLoan); // Sla de lening op
    }
}