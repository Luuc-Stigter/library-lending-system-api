package com.luucstigter.library_lending_system_api.repository;

import com.luucstigter.library_lending_system_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // TODO later custom queries toevoegen, bijvoorbeeld: List<Book> findByAuthor(String author);
}
