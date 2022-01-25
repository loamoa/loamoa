package com.loamoa.loamoa.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.repository.ItemRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@Service
public class ItemService {
    private static ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public static ArrayList<Item> getItemList() {
        return itemRepository.returnItemList();
    }
    public static Item saveItem(Item item) {
        itemRepository.save(item);
        return item;
    }

    public static void clearItem() {
        itemRepository.clearStore();
    }

    public JSONObject getJsonObject(JSONObject obj, String key) {
        JSONObject result = obj.getJSONObject(key);
        return result;
    }

    public static Item setItemInfo(JSONObject obj, Item item) {
        item.setName(obj.getString("Name"));
        item.setUpgrade(obj.getString("Upgrade"));
        item.setParts(obj.getString("Parts"));
        item.setLevel(obj.getString("Level"));
        item.setQuality(obj.getInt("Quality"));
        item.setIcon(obj.getString("Icon"));
        return item;
    }
    public static Item setItemEngrave(JSONObject obj, Item item) {
        if(obj.has("0")) {
            JSONObject curObj = obj.getJSONObject("0");
            item.setEngrave1_name(curObj.getString("Name"));
            item.setEngrave1_value(curObj.getString("Value"));
        }
        if(obj.has("1")) {
            JSONObject curObj = obj.getJSONObject("1");
            item.setEngrave2_name(curObj.getString("Name"));
            item.setEngrave2_value(curObj.getString("Value"));
        }
        return item;
    }

    public String checkItemInfo(String username) {
        RestTemplate restTemplate = new RestTemplate();
        URI calculateURI = buildURI(username);
        String result = restTemplate.getForObject(calculateURI, String.class);
        return result;
    }

    private URI buildURI(String username) {
        String endpointURI = "http://apis.iptime.org/LostArk/Character/Character-Item";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpointURI)
                .queryParam("NickName", username);
        return builder.build().encode().toUri();
    }
}
