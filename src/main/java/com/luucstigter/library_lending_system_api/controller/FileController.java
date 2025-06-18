package com.luucstigter.library_lending_system_api.controller;

import com.luucstigter.library_lending_system_api.model.DigitalBookFile;
import com.luucstigter.library_lending_system_api.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileController {

    private final ItemService itemService;

    public FileController(ItemService itemService) {
        this.itemService = itemService;
    }

    // Endpoint om een bestand te uploaden en te koppelen aan een item
    @PostMapping("/items/{itemId}/file")
    public ResponseEntity<Object> uploadFileForItem(@PathVariable Long itemId, @RequestParam("file") MultipartFile file) {
        DigitalBookFile uploadedFile = itemService.assignFileToItem(itemId, file);

        // Stuur een bericht terug met de naam van het opgeslagen bestand
        String message = "File succesvol geupload: " + uploadedFile.getStoredFileName();
        return ResponseEntity.ok().body(message);
    }

    // Endpoint om een item op te vragen, zodat we de file-info kunnen zien
    @GetMapping("/items/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }
}