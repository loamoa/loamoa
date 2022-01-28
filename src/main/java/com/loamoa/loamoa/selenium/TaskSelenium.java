package com.loamoa.loamoa.selenium;

import com.loamoa.loamoa.domain.Account;
import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.MarketForm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;

import static com.loamoa.loamoa.utility.Utills.toFloatNumber;

@Component
public class TaskSelenium {

    private WebDriver driver;
    private WebDriverWait wait;

    public TaskSelenium() {
        this("selenium/chromedriver_97.0.4692.71/chromedriver"); // 기본 driverPath
    }

    public TaskSelenium(String driverPath) {
        URL resource; // driverPath 경로의 리소스 파일
        Optional<String> filePath = null; // driverPath 경로

        // driverPath에 chromedriver가 존재하는지 확인
        try {
            resource = getClass().getClassLoader().getResource(driverPath);
            filePath = Optional.ofNullable(resource.getFile()); // NullPointException 가능
        } catch (NullPointerException e) {
            System.out.println("경로 " + driverPath + "가 존재하지 않습니다.");
        }

        // System Parameter 생성
        String WEB_DRIVER_ID = "webdriver.chrome.driver"; // 크롬 드라이버 사용
        String WEB_DRIVER_PATH = filePath.get(); // 크롬 드라이버 경로
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH); // 시스템에 드라이버 연결

        // 크롬 옵션을 위한 ChromeOptions 인스턴스 생성
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true);
        options.addArguments("--headless"); // 브라우저 창 없이 실행
        options.addArguments("--no-sandbox"); // 크롬 보안모드 해제
        options.addArguments("disable-dev-shm-usage"); // shared memory 사용 중지
        options.addArguments("lang=ko"); // 서버 동작을 위해 언어 설정
        options.addArguments("window-size=1920x1080"); // 윈도우 창 크기 설정
        options.addArguments("disable-gpu"); // 그래픽카드 가속 끄기

        driver = new ChromeDriver(options); // 옵션 포함한 드라이버 생성
        wait = new WebDriverWait(driver,10); // 드라이버가 최대 5초동안 기다림

        System.out.println(driver.getCurrentUrl());
    }

    /**
     * 거래소 검색 중 로스트아크 홈페이지에 로그인되지 않았다면 로그인을 시도한다.
     */
    public void loginHomePage() {
        try {
            // 로그인 페이지일때만 처리
            if (driver.getTitle().equals("STOVE")) {
                // 계정 정보 불러오기
                Account account = getLoginAccount().get();
                String id = account.getId();
                String pw = account.getPw();

                // 아이디 입력
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_id"))).sendKeys(id);
                // 패스워드 입력
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_pwd"))).sendKeys(pw);
                // 로그인
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#idLogin > div.row.grid.el-actions > button"))).click();

                // 로그인 할 시간 기다리기
                Thread.sleep(1000);
            } else {
                System.out.println("[loginHomePage] 이미 로그인 상태입니다.");
            }
            // 거래소 검색 결과 화면인지 확인
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#itemList")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 거래소에서 검색하고자 하는 물품의 FormData에 맞는 검색 결과를 리턴한다.
     * @param form 검색하고자 하는 물품의 FromData
     * @return 검색된 아이템
     */
    public Optional<Item> getItem(MarketForm form) {
        Item item = new Item();
        try {
            String baseUrl = getMarketUrl(form);
            System.out.println("[getItem]" + baseUrl);
            WebElement element;

            driver.get(baseUrl); // 검색 URL로 연결
            loginHomePage(); // 만약 로그인 창이 뜬다면 로그인

            // 이름 저장
            element = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(1) > div > span.name"));
            item.setName(element.getText());
            // 전일 평균 거래가 저장
            element = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(2) > div > em"));
            item.setDailyTradingPrice(toFloatNumber(element.getText()));
            // 최근 거래가 저장
            element = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(3) > div > em"));
            item.setRecentTradingPrice(toFloatNumber(element.getText()));
            // 현재 최저가 저장
            element = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(4) > div > em"));
            item.setCurrentMinPrice(toFloatNumber(element.getText()));
        } catch (Exception e) {
            // 검색 중 오류 발생시 null item 리턴
            item = null;
            e.printStackTrace();
        }
        return Optional.ofNullable(item);
    }

    /*
     * 크롤링 시 사용하는 유틸리티 메소드
     */
    /**
     * login.json 파일에 저장된 홈페이지 로그인을 위한 id, pw를 가져온다.
     * NullPointerException 방지를 위하여 Optional로 리턴한다.
     * @return Optional<Account> id,pw 정보가 포함된 Account 객체를 반환한다.
     */
    public Optional<Account> getLoginAccount() {
        String id = null, pw = null;
        // resource/selenium/login/login.json 에서 id,pw 정보를 가져옴
        ClassPathResource resource = new ClassPathResource("selenium/login/login.json");
        try {
            JSONObject user = (JSONObject) new JSONParser().parse(
                    new InputStreamReader(resource.getInputStream(), "UTF-8"));
            id = user.get("id").toString();
            pw = user.get("pw").toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Optional.of(new Account(id,pw));
    }

    /**
     * FormData를 검색할 수 있는 URL로 변환한다.
     * @param marketForm 거래소에서 검색하기 위한 FormData
     * @return FormData에 대한 검색 결과를 얻을 수 있는 Url
     */
    public String getMarketUrl(MarketForm marketForm) {
        return "https://lostark.game.onstove.com/Market/List_v2?" + marketForm.toString();
    }
}
