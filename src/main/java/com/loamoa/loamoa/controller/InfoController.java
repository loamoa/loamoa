package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.User;
import com.loamoa.loamoa.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InfoController {

    private final ItemService itemService;

    @Autowired
    public InfoController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("")
    public String movePage() {
        return "redirect:/input";
    }
    @GetMapping("/input")
    public String inputNicknameForm() {
        return "/test/inputName";
    }

    @PostMapping("/input/search")
    public String search(UserForm form, Model model) {
        String uname = form.getUsername();
        String result = itemService.checkItemInfo(form.getUsername());
        model.addAttribute("Items", result);
        return "/test/searchresult";
    }
}
