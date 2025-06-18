package com.luucstigter.library_lending_system_api.repository;

import com.luucstigter.library_lending_system_api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByBookId(Long bookId);
}