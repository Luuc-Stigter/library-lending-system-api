package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.exception.ResourceConflictException;
import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import com.luucstigter.library_lending_system_api.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public BookService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden met id: " + id));
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = getBookById(id);

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublisher(bookDetails.getPublisher());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setGenre(bookDetails.getGenre());

        return bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boek niet gevonden met id: " + id));

        for (Item item : book.getItems()) {
            boolean hasActiveLoan = loanRepository.existsByItemAndStatus(item, "ACTIVE");
            if (hasActiveLoan) {
                throw new ResourceConflictException("Kan boek niet verwijderen. Minstens één exemplaar (ID: " + item.getId() + ") is momenteel uitgeleend.");
            }
        }

        bookRepository.deleteById(id);
    }
}