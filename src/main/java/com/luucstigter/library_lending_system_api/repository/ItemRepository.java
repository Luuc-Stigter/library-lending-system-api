package com.luucstigter.library_lending_system_api.repository;

import com.luucstigter.library_lending_system_api.model.Item;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}