package com.loamoa.loamoa.service;

import com.loamoa.loamoa.driver.ItemPriceDriver;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebElement;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.List;

public class ItemPriceService {
    private final ItemPriceDriver driver;
    private String id;
    private String pw;

    public ItemPriceService() {
        driver = new ItemPriceDriver();

        // ID/PW의 보안성을 위해 Json 에서 Load (추 후에 따로 뺼 예쩡)
        ClassPathResource resource = new ClassPathResource("login/user.json");
        try {
            JSONObject user = (JSONObject) new JSONParser().parse(
                    new InputStreamReader(resource.getInputStream(), "UTF-8"));

            id = (String) user.get("id");
            pw = (String) user.get("pw");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * LostArk에 로그인
     */
    public void loginToLostArk() {
        driver.runDriver();
        driver.login(id, pw);
    }

    public List<WebElement> getItemPrice(String item) {
        List<WebElement> itemPrices;

        loginToLostArk();
        // 경매장 클릭
        driver.clickByCssSelector("#lostark-wrapper > header > nav > ul > li:nth-child(6) > a");

        // 해당 아이템 입력
        driver.sendKeyByCssSelector("#txtItemName", item);

        // 검색
        driver.clickByCssSelector("#lostark-wrapper > div > main > div > div.deal > div.deal-contents > " +
                "form > fieldset > div > div.bt > button.button.button--deal-submit");

        // 결과물 반환
        itemPrices = driver.getElementsByCssSelector("price");
        System.out.println("[ItemPriceServices]" + itemPrices);
        for (WebElement e : itemPrices) {
            System.out.println(e.getText());
        }
        return itemPrices;
    }
}
