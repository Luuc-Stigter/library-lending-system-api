package com.luucstigter.library_lending_system_api.controller;

import com.luucstigter.library_lending_system_api.model.Item;
import com.luucstigter.library_lending_system_api.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/books/{bookId}/items")
    public ResponseEntity<Item> addItemToBook(@PathVariable Long bookId, @RequestBody Item item) {
        Item newItem = itemService.addItemToBook(bookId, item);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/items/{id}")
                .buildAndExpand(newItem.getId()).toUri();

        return ResponseEntity.created(location).body(newItem);
    }

    @GetMapping("/books/{bookId}/items")
    public ResponseEntity<List<Item>> getItemsByBookId(@PathVariable Long bookId) {
        List<Item> items = itemService.getItemsByBookId(bookId);
        return ResponseEntity.ok(items);
    }
}