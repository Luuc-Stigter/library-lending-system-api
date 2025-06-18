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

    @Test
    void testDeleteBookSuccess() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        // Roep de methode aan. We verwachten geen return-waarde.
        bookService.deleteBook(1L);

        // Assert
        // Verifiëer dat de deleteById methode exact 1 keer is aangeroepen met het juiste ID.
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBookThrowsException() {
        // Arrange
        // Simuleer dat het boek niet bestaat.
        when(bookRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        // Controller of de juiste exceptie wordt gegooid.
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(99L);
        });

        // Assert (extra controles)
        assertEquals("Boek niet gevonden met id: 99", exception.getMessage());
        // Verifiëer dat de deleteById methode NOOIT is aangeroepen.
        verify(bookRepository, never()).deleteById(99L);
    }

    @Test
    void testUpdateBookSuccess() {
        // Arrange
        // Maak een DTO object met de nieuwe details
        Book updateDetails = new Book();
        updateDetails.setTitle("Dune Messiah");
        updateDetails.setAuthor("Frank Herbert");
        updateDetails.setPublicationYear(1969);
        updateDetails.setIsbn(book1.getIsbn());
        updateDetails.setGenre(book1.getGenre());
        updateDetails.setPublisher(book1.getPublisher());


        // Simuleer dat het boek met ID 1 bestaat
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        // Simuleer dat het opslaan het bijgewerkte boek teruggeeft
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Book result = bookService.updateBook(1L, updateDetails);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dune Messiah", result.getTitle()); // Controleer de gewijzigde titel
        assertEquals(1969, result.getPublicationYear()); // Controleer het gewijzigde jaar
        verify(bookRepository, times(1)).save(any(Book.class)); // Controleer of save is aangeroepen
    }

    @Test
    void testUpdateBookThrowsException() {
        // Arrange
        Book updateDetails = new Book();
        // Simuleer dat het boek met ID 99 niet bestaat
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        // Controleer of de juiste exceptie wordt gegooid, omdat getBookById wordt aangeroepen
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(99L, updateDetails);
        });

        // Assert (extra controle)
        // Verifiëer dat de save methode NOOIT is aangeroepen, omdat de code faalde voordat we bij het opslaan kwamen.
        verify(bookRepository, never()).save(any(Book.class));
    }
}