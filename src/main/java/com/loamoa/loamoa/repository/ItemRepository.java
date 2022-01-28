package com.loamoa.loamoa.repository;

import com.loamoa.loamoa.domain.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);
    List<Item> findAll();
    Optional<Item> findByName(String itemName);
    List<String> findAllItemName(int type);
}
