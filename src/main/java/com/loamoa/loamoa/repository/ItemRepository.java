package com.loamoa.loamoa.repository;

import com.loamoa.loamoa.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class ItemRepository {
    private ArrayList<Item> store = new ArrayList<>();

    public Item save(Item item) {
        store.add(item);
        return item;
    }
    
    public ArrayList<Item> returnItemList() {
        return store;
    }
    public void clearStore() {
        store.clear();
    }
}
