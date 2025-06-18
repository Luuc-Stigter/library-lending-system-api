package com.luucstigter.library_lending_system_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Corresponds to item_id

    // Enum zou hier nog beter zijn, maar voor nu is String prima
    private String type; // e.g., "HARDCOVER", "PAPERBACK", "EBOOK_PDF"
    private String status; // e.g., "AVAILABLE", "ON_LOAN", "RESERVED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore // HEEL BELANGRIJK!
    private Book book;

    // Getters en Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}