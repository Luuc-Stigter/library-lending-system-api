package com.luucstigter.library_lending_system_api.config;

import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.model.User;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import com.luucstigter.library_lending_system_api.repository.ItemRepository;
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, BookRepository bookRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Voer de seeder alleen uit als de database leeg is om duplicaten te voorkomen
        if (userRepository.count() == 0 && bookRepository.count() == 0) {
            System.out.println("Database is leeg. Bezig met vullen van testdata...");
            seedUsers();
            seedBooksAndItems();
            System.out.println("Testdata succesvol toegevoegd.");
        } else {
            System.out.println("Database bevat al data. Seeder wordt overgeslagen.");
        }
    }

    private void seedUsers() {
        // Gebruiker 1: Lener (Member)
        User member = new User();
        member.setUsername("member");
        // Hashn het wachtwoord direct met de PasswordEncoder
        member.setPassword(passwordEncoder.encode("password"));
        member.setEmail("member@library.nl");
        member.setRole("MEMBER");
        member.setMembershipStartDate(LocalDate.parse("2024-01-15"));
        userRepository.save(member);

        // Gebruiker 2: Bibliothecaris (Librarian)
        User librarian = new User();
        librarian.setUsername("librarian");
        librarian.setPassword(passwordEncoder.encode("password"));
        librarian.setEmail("librarian@library.nl");
        librarian.setRole("LIBRARIAN");
        librarian.setMembershipStartDate(LocalDate.parse("2023-05-20"));
        userRepository.save(librarian);
    }

    private void seedBooksAndItems() {
        // Boek 1: Dune
        Book dune = new Book();
        dune.setTitle("Dune");
        dune.setAuthor("Frank Herbert");
        dune.setIsbn("978-0441013593");
        dune.setPublisher("Ace Books");
        dune.setPublicationYear(1965);
        dune.setGenre("Science Fiction");

        // Exemplaren voor Dune
        Item duneHardcover = new Item();
        duneHardcover.setType("HARDCOVER");
        duneHardcover.setStatus("AVAILABLE");
        duneHardcover.setBook(dune);

        Item duneEbook = new Item();
        duneEbook.setType("EBOOK_PDF");
        duneEbook.setStatus("AVAILABLE");
        duneEbook.setBook(dune);

        dune.setItems(Set.of(duneHardcover, duneEbook));

        // Sla het boek op.
        bookRepository.save(dune);


        // Boek 2: The Lord of the Rings
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
        bookRepository.save(lotr);
    }
}