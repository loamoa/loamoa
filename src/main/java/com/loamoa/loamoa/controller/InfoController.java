package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.User;
import com.loamoa.loamoa.repository.ItemRepository;
import com.loamoa.loamoa.service.ItemService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

@Controller
public class InfoController {

    private ItemService itemService;

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
    public String search(UserForm form, Model model)  {
        ItemService.clearItem();
        String uname = form.getUsername();
        LinkedHashMap<String, JSONObject> equipment = itemService.itemCrawling(uname);
        for (Map.Entry<String, JSONObject> entry : equipment.entrySet()) {
            Item curItem = new Item();
            JSONObject curJson = entry.getValue();
            curItem = itemService.setItemInfo(curJson, curItem);
            ItemService.saveItem(curItem);
        }
//        String result = itemService.checkItemInfo(form.getUsername());
//        String[] parts_arr = {"머리 방어구", "어깨 방어구", "상의", "하의", "장갑","무기",
//                                "목걸이", "귀걸이1", "귀걸이2", "반지1", "반지2"
//                                };
//        ArrayList<String> parts = new ArrayList<>(Arrays.asList(parts_arr));
//        LinkedHashMap<String, JSONObject> jsonMap = new LinkedHashMap<>();
//        LinkedHashMap<String, JSONObject> jsonOptionMap = new LinkedHashMap<>();
//
//        JSONObject obj = new JSONObject(result);
//        JSONObject objItem = itemService.getJsonObject(obj, "Items");
//        for(String part: parts) {
//            if(objItem.has(part)) jsonMap.put(part, objItem.getJSONObject(part));
//        }
//        for (Map.Entry<String, JSONObject> entry : jsonMap.entrySet()) {
//            Item curItem = new Item();
//            JSONObject curJson = entry.getValue();
//            JSONObject curOption = curJson.getJSONObject("Option");
//            curItem = ItemService.setItemInfo(curJson, curItem);
//            if(curOption.has("Engraving Effects")) {
//                curItem = ItemService.setItemEngrave
//                        (curOption.getJSONObject("Engraving Effects"), curItem);
//            }
//            ItemService.saveItem(curItem);
//        }
        model.addAttribute("username", uname);
        model.addAttribute("items", ItemService.getItemList());
        return "/test/searchresult";
    }
}
