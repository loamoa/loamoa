package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.service.ItemPriceService;
import org.openqa.selenium.WebElement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PriceController {

    @GetMapping("/mamagement/itemPrice")
    public String getItemPrice(@RequestParam("item") String item) {
        ItemPriceService itemPriceService = new ItemPriceService();
        String string = "";

        for(WebElement e : itemPriceService.getItemPrice(item)) {
            string += e.getText();
        }
        return string;
    }
}
