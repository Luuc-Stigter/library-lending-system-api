package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import com.luucstigter.library_lending_system_api.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final BookRepository bookRepository;

    public ItemService(ItemRepository itemRepository, BookRepository bookRepository) {
        this.itemRepository = itemRepository;
        this.bookRepository = bookRepository;
    }

    // Een item (exemplaar) toevoegen aan een specifiek boek
    public Item addItemToBook(Long bookId, Item item) {
        // Zoek het boek op. Gooi een exception als het niet bestaat.
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        // Koppel het boek aan het nieuwe item
        item.setBook(book);

        // Sla het nieuwe item op in de database
        return itemRepository.save(item);
    }

    // Alle items voor een specifiek boek opvragen
    public List<Item> getItemsByBookId(Long bookId) {
        // Controleer eerst of het boek wel bestaat
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found with id: " + bookId);
        }
        // Vraag alle items op die bij dit bookId horen
        return itemRepository.findByBookId(bookId);
    }
}