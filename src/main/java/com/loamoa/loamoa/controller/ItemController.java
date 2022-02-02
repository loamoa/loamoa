package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.domain.SkillSet;
import com.loamoa.loamoa.service.AuctionService;
import com.loamoa.loamoa.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    ItemService itemService;
    AuctionService auctionService;

    public ItemController(ItemService itemService, AuctionService auctionService) {
        this.itemService = itemService;
        this.auctionService = auctionService;
    }

    //    @Autowired
//    public ItemController(ItemService itemService) {
//        this.itemService = itemService;
//    }

    @GetMapping("/item/findall")
    public void findall() {
        itemService.findItems();
    }

    @GetMapping("/item/update")
    public void update() {
        itemService.refreshItems();
    }

    @GetMapping("/db/setskill")
    public void characterSkillSave() {
        auctionService.addCharacterSkills();
    }

    @GetMapping("/character/findSkills")
    public void characterSkillsList() {
//        List<SkillSet>
    }

    @GetMapping(value = "/character/findskill")
    public void characterSkillList(@RequestParam(value="id", defaultValue="haenny") String characterName) {

    }

}

