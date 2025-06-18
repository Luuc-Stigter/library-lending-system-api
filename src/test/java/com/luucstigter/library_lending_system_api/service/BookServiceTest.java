package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock // Nep versie van de BookRepository
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    // Herbruikbare test objecten
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Dune");
        book1.setAuthor("Frank Herbert");

        book2 = new Book();
        book2.setId(2L);
        book2.setTitle("The Lord of the Rings");
        book2.setAuthor("J.R.R. Tolkien");
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

        // Roep de te testen methode aan
        List<Book> result = bookService.getAllBooks();

        // Controleer het resultaat
        assertEquals(2, result.size());
        assertEquals("Dune", result.get(0).getTitle());
    }

    @Test
    void testGetBookByIdSuccess() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dune", result.getTitle());
    }

    @Test
    void testGetBookByIdThrowsException() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Controleer of de juiste exceptie wordt gegooid
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });

        // Controleer of de exceptie de juiste boodschap heeft
        assertEquals("Boek niet gevonden met id: 99", exception.getMessage());
    }

    @Test
    void testAddBook() {

        Book newBook = new Book();
        newBook.setTitle("Foundation");
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        Book savedBook = bookService.addBook(newBook);

        assertEquals("Foundation", savedBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}