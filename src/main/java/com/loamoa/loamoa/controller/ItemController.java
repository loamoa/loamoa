package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/item/findall")
    public void findall() {
        itemService.findItems();
    }

    @GetMapping("/item/update")
    public void update() {
        itemService.refreshItems();
    }
}

