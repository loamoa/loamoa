package com.loamoa.loamoa.service;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.repository.ItemRepository;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
        item.setName(obj.getString("name"));
//        item.setUpgrade(obj.getString("Upgrade"));
//        item.setParts(obj.getString("Parts"));
//        item.setLevel(obj.getString("Level"));
//        item.setQuality(obj.getInt("Quality"));
        item.setIcon(obj.getString("image"));
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

    public LinkedHashMap<String, JSONObject> itemCrawling(String username) {
        String url = "https://lostark.game.onstove.com/Profile/Character/" + username;
        Document doc = null;
        String scriptString = null;
        ArrayList<String> equipCodeArray = new ArrayList<>();
        LinkedHashMap<String, JSONObject> codeObjMap = new LinkedHashMap<>();
        /*
         ** JSoup 연결
         */
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
         ** <Script> 인포 불러오기
         * Script 태그를 가져와, DataNode로 변환 뒤, 장비 데이터를 WholeData()로 가져옴.
         * 가져온 String을 Substring을 이용해 JSON으로 변환할 수 있게끔 변환
         */
        for (Element element : doc.getElementsByTag("script")){
            for(DataNode node : element.dataNodes()) {
                if(node.getWholeData().contains("Card")) {
                    scriptString = node.getWholeData();
                    while (scriptString.charAt(0) != '{') {
                        scriptString = scriptString.substring(1);
                    }
                }
            }
        }

        /*
        ** 장비하고 있는 장비의 장비코드 추출
         */
        JSONObject obj = new JSONObject(scriptString);
        JSONObject equipment = (JSONObject) obj.get("Equip"); // Equip 태그 JSON 추출
        Iterator<String> keys = equipment.keys();
        Elements elementEquip = doc.select("div.profile-equipment__slot").select("div");

        /*
        * 착용하고 있는 장비칸에 대해서 이미지 정보를 담는 과정
        * 가끔 null값이 존재하는 경우가 있어, 길이가 3 미만이면 진행 처리
        * data-grade가 0이면 (아바타, 보석, 어빌리티 스톤의 각인이 해당함) 진행 처리
        * codeObjMap(아이템 코드, 정보를 담는 JSON)에 이미지 링크를 담음.
        * 이미지 링크는 Script 내에 존재하지 않고 Html 태그 내에 존재하여 아래와 같이 따로 처리
         */
        for(Element element : elementEquip) {
            if(element.attr("data-grade").equals("0")) continue;
            if(element.attr("data-item").length() < 3) continue;
            try {
                JSONObject imageJson = new JSONObject();
                imageJson.put("image", element.select("img").attr("src"));
                codeObjMap.put(element.attr("data-item"), imageJson);
            } catch(NullPointerException e) {
                continue;
            }
        }

        /*
         * Script에서 뽑아낸 장비코드 (Key)에 대해서 진행
         * 스크립트의 Equip 항목에는 장비 뿐만이 아니라, 아바타 보석 등 여러 장비들이 포함되어 있어 JSON의 구조가 각각 다름.
         * #1) 위에서 장비들의 해당하는 Key들만 codeObjMap에 넣어놓았으므로, 맵에서 키를 찾을 수 없다면 보석 or 아바타라는 이야기가 됨.
         * #1의 경우에는 continue로 계속 진행
         * Element_000의 Value에는 장비명이 있음을 확인가능 (웹페이지 소스 참고)
         * Html 폼 형식으로 이루어져 있으므로 P 태그를 제거하여 JSON에 추가 삽입
         */
        while(keys.hasNext()) {
            String key = keys.next();
            if(!codeObjMap.containsKey(key)) {
                continue;
            }
            JSONObject curJson = getJsonObject(equipment, key);
            String parseName = getJsonObject(curJson, "Element_000").getString("value");
            Document html = Jsoup.parse(parseName);
            String equipmentName = html.select("P").text();
            System.out.println(equipmentName);
            JSONObject newJson = codeObjMap.get(key);
            newJson.put("name", equipmentName);
            codeObjMap.put(key, newJson);
        }
        System.out.println(codeObjMap);
        return codeObjMap;
    }
}
