package com.luucstigter.library_lending_system_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @OneToOne(
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private DigitalBookFile digitalBookFile;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
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
    public DigitalBookFile getDigitalBookFile() { return digitalBookFile; }
    public void setDigitalBookFile(DigitalBookFile digitalBookFile) { this.digitalBookFile = digitalBookFile; }
}