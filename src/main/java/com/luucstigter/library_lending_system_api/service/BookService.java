package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book addBook(Book book) {
        // TODO later validatie toevoegen, bv. controleren of ISBN al bestaat.
        return bookRepository.save(book);
    }
}
