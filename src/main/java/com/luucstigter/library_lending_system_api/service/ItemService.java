package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.Book;
import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.repository.BookRepository;
import com.luucstigter.library_lending_system_api.repository.ItemRepository;
import org.springframework.stereotype.Service;
import com.luucstigter.library_lending_system_api.model.DigitalBookFile;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import com.luucstigter.library_lending_system_api.exception.ResourceConflictException;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final BookRepository bookRepository;
    private final FileStorageService fileStorageService;

    public ItemService(ItemRepository itemRepository, BookRepository bookRepository, FileStorageService fileStorageService) {
        this.itemRepository = itemRepository;
        this.bookRepository = bookRepository;
        this.fileStorageService = fileStorageService;
    }

    // Een item (exemplaar) toevoegen aan een specifiek boek
    public Item addItemToBook(Long bookId, Item item) {
        // Zoek het boek op. Gooi een exception als het niet bestaat.
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("boek niet gevonden met id: " + bookId));

        // Koppel het boek aan het nieuwe item
        item.setBook(book);

        // Sla het nieuwe item op in de database
        return itemRepository.save(item);
    }

    // Alle items voor een specifiek boek opvragen
    public List<Item> getItemsByBookId(Long bookId) {
        // Controleer eerst of het boek wel bestaat
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("boek niet gevonden met id: " + bookId);
        }
        // Vraag alle items op die bij dit bookId horen
        return itemRepository.findByBookId(bookId);
    }

    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item niet gevonden met id: " + itemId));
    }

    public DigitalBookFile assignFileToItem(Long itemId, MultipartFile file) {
        // 1. Zoek het item op
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item niet gevonden met id: " + itemId));

        // Controleer of dit item al een gekoppeld bestand heeft.
        if (item.getDigitalBookFile() != null) {
            throw new ResourceConflictException("Item met id " + itemId + " heeft al een gekoppelde file.");
        }

        // 2. Sla het fysieke bestand op met de FileStorageService
        String storedFileName = fileStorageService.storeFile(file);

        // 3. Maak de DigitalBookFile entiteit aan met de metadata
        DigitalBookFile digitalBookFile = new DigitalBookFile();
        digitalBookFile.setOriginalFileName(file.getOriginalFilename());
        digitalBookFile.setStoredFileName(storedFileName);
        digitalBookFile.setFileType(file.getContentType());
        digitalBookFile.setSize(file.getSize());
        digitalBookFile.setUploadDate(LocalDate.now());
        digitalBookFile.setItem(item); // Koppel aan het item

        // 4. Update de relatie in het Item-object
        item.setDigitalBookFile(digitalBookFile);
        itemRepository.save(item); // Dankzij CascadeType.ALL wordt de DigitalBookFile ook opgeslagen

        return digitalBookFile;
    }
}