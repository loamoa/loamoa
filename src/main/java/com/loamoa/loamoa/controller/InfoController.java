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

        String endpointURI = "http://apis.iptime.org/LostArk/Character/Character-Item";
        String jewel_endpointURI =  "http://apis.iptime.org/LostArk/Character/Character-Jewel";

        String result = itemService.checkItemInfo(form.getUsername(),endpointURI);
        String jewel_result = itemService.checkItemInfo(form.getUsername(),jewel_endpointURI);

        String[] parts_arr = {"머리 방어구", "어깨 방어구", "상의", "하의", "장갑","무기",
                                "목걸이", "귀걸이1", "귀걸이2", "반지1", "반지2"
                                };

        String[] jewel_arr = { "1번째 보석","2번째 보석","3번째 보석","4번째 보석","5번째 보석","6번째 보석","7번째 보석","8번째 보석",
                            "9번째 보석","10번째 보석","11번째 보석"
                            };
        ArrayList<String> parts = new ArrayList<>(Arrays.asList(parts_arr));
        ArrayList<String> jewels = new ArrayList<>(Arrays.asList(jewel_arr));

        LinkedHashMap<String, JSONObject> jsonMap = new LinkedHashMap<>();
        LinkedHashMap<String, JSONObject> jsonMap_Jewel = new LinkedHashMap<>();

        LinkedHashMap<String, JSONObject> jsonOptionMap = new LinkedHashMap<>();

        JSONObject obj = new JSONObject(result);
        JSONObject obj_jewel = new JSONObject(jewel_result);

        JSONObject objItem = itemService.getJsonObject(obj, "Items");
        JSONObject objJewel = itemService.getJsonObject(obj_jewel, "Jewel");

        for(String part: parts) {
            jsonMap.put(part, objItem.getJSONObject(part));
        }
        for (Map.Entry<String, JSONObject> entry : jsonMap.entrySet()) {
            Item curItem = new Item();
            JSONObject curJson = entry.getValue();

            JSONObject curOption = curJson.getJSONObject("Option");
            if(curOption.has("Engraving Effects")) {
                curItem = ItemService.setItemEngrave(curOption.getJSONObject("Engraving Effects"), curItem);
                }

            curItem = ItemService.setItemInfo(curJson, curItem);

            ItemService.saveItem(curItem);
        }

        for (String jewel : jewels){
            jsonMap_Jewel.put(jewel, objJewel.getJSONObject(jewel));
        }
        for (Map.Entry<String, JSONObject> entry : jsonMap_Jewel.entrySet()) {
            Item curItem = new Item();
            JSONObject curJson = entry.getValue();
            curItem = ItemService.setJewelInfo(curJson, curItem);
            ItemService.saveItem(curItem);
        }
        model.addAttribute("username", uname);
        model.addAttribute("items", ItemService.getItemList());
        return "/test/searchresult";
    }
}
