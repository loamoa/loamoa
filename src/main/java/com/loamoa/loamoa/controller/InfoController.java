package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.User;
import com.loamoa.loamoa.service.ItemService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public String search(UserForm form, Model model)  {
        Item head = new Item();
        Item shoulder = new Item();
        Item clothes = new Item();
        Item pants = new Item();
        Item gloves = new Item();
        Item weapon = new Item();

        String uname = form.getUsername();
        String result = itemService.checkItemInfo(form.getUsername());
        JSONObject obj = new JSONObject(result);
        JSONObject objItem = obj.getJSONObject("Items");
        JSONObject objItemHead = objItem.getJSONObject("머리 방어구");
        JSONObject objItemShoulder = objItem.getJSONObject("어깨 방어구");
        JSONObject objItemClothes = objItem.getJSONObject("상의");
        JSONObject objItemPants = objItem.getJSONObject("하의");
        JSONObject objItemGloves = objItem.getJSONObject("장갑");
        JSONObject objItemWeapon = objItem.getJSONObject("무기");
        String headItem = objItemHead.getString("Name") +
                            objItemHead.getString("Upgrade");

        String shoulderItem = objItemShoulder.getString("Name") +
                            objItemShoulder.getString("Upgrade");

        String clothesItem = objItemClothes.getString("Name") +
                                objItemClothes.getString("Upgrade");

        String pantsItem = objItemPants.getString("Name") +
                            objItemPants.getString("Upgrade");

        String glovesItem = objItemGloves.getString("Name") +
                            objItemGloves.getString("Upgrade");

        String weaponItem = objItemWeapon.getString("Name") +
                            objItemWeapon.getString("Upgrade");

        ArrayList<String> equipment = new ArrayList<String>();
        ArrayList<String> equipimage = new ArrayList<String>();
        head.setName(headItem);
        shoulder.setName(shoulderItem);
        clothes.setName(clothesItem);
        pants.setName(pantsItem);
        gloves.setName(glovesItem);
        weapon.setName(weaponItem);
        head.setIcon(objItemHead.getString("Icon"));
        shoulder.setIcon(objItemShoulder.getString("Icon"));
        clothes.setIcon(objItemClothes.getString("Icon"));
        pants.setIcon(objItemPants.getString("Icon"));
        gloves.setIcon(objItemGloves.getString("Icon"));
        weapon.setIcon(objItemWeapon.getString("Icon"));

        ArrayList<Item> arr = new ArrayList<Item>();
        arr.add(head);
        arr.add(shoulder);
        arr.add(clothes);
        arr.add(pants);
        arr.add(gloves);
        arr.add(weapon);

        model.addAttribute("username", uname);
        model.addAttribute("items", arr);

        return "/test/searchresult";
    }
}
