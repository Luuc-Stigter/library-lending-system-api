package com.luucstigter.library_lending_system_api.controller;

import com.luucstigter.library_lending_system_api.model.DigitalBookFile;
import com.luucstigter.library_lending_system_api.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.luucstigter.library_lending_system_api.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class FileController {

    private final ItemService itemService;

    public FileController(ItemService itemService, FileStorageService fileStorageService) {
        this.itemService = itemService;
        this.fileStorageService = fileStorageService;
    }
    private final FileStorageService fileStorageService;

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

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Laad het bestand als een Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Probeer de content type van het bestand te bepalen
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("file type niet gevonden.");
        }

        // Fallback naar de standaard content type als het niet bepaald kan worden
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}