package com.loamoa.loamoa.selenium;

import com.loamoa.loamoa.domain.Account;
import com.loamoa.loamoa.domain.Item;
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

@Component
public class TaskSelenium {

    private WebDriver driver;
    private WebDriverWait wait;

    public TaskSelenium() {
        // 기본 driverPath 입력
        this("selenium/chromedriver_97.0.4692.71/chromedriver");
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

        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"); // Headless임을 숨기기 위한 수정 값

        driver = new ChromeDriver(options); // 옵션 포함한 드라이버 생성
        wait = new WebDriverWait(driver,10); // 드라이버가 최대 5초동안 기다림
    }

    /**
     * 셀레니움 드라이버를 종료한다.
     */
    public void stopSelenium() {
        try {
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * login.json 파일에 저장된 홈페이지 로그인을 위한 id, pw를 가져온다.
     * NullPointerException 방지를 위하여 Optional로 리턴한다.
     * @return Optional<Account> id,pw 정보가 포함된 Account 객체를 반환한다.
     */
    public Optional<Account> getLoginAccount() {
        String id = null, pw = null;
        // resource/selenium/login/user.json 에서 id,pw 정보를 가져옴
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
     * 이름이 itemName 인 각인서 아이템을 검색한다.
     * @param itemName 검색하려는 각인서 이름
     * @return Optional<Item> 검색한 아이템 객체를 반환한다.
     */
    public Optional<Item> getBookItemByName(String itemName) {
        WebElement itemInfo;
        Item item = new Item();
        try {
            String baseUrl = "https://lostark.game.onstove.com/Market/List_v2?"
                    +"firstCategory=0&secondCategory=0&characterClass=&tier=0&grade=4" // 전설 등급 각인서 중에
                    +"&itemName="+itemName // itemName이라는 이름의 아이템 검색
                    +"&pageNo=1&isInit=false&sortType=2";
            driver.get(baseUrl); // 해당 탭 열기
            loginHomePage(); // 로그인이 되어있지 않다면 로그인

            // 이름 저장
            itemInfo = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(1) > div > span.name"));
            item.setName(itemInfo.getText());

            // 전일 평균 거래가 저장
            itemInfo = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(2) > div > em"));
            item.setDailyTradingPrice(toFloatNumber(itemInfo.getText()));

            // 최근 거래가 저장
            itemInfo = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(3) > div > em"));
            item.setRecentTradingPrice(toFloatNumber(itemInfo.getText()));

            // 현재 최저가 저장
            itemInfo = driver.findElement(By.cssSelector("#tbodyItemList > tr > td:nth-child(4) > div > em"));
            item.setCurrentMinPrice(toFloatNumber(itemInfo.getText()));

            // item 객체에 값 저장
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.of(item);
    }

    public void closePopup() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#modal-present > div > div > div.lui-modal__button > button.button--close"))).click();
            System.out.println("[closePopup] close popup");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 로스트아크 홈페이지에 로그인되지 않았다면 로그인을 시도합니다.
     */
    public void loginHomePage() {
        try {
            // 로그인 페이지일때만 처리
            if (driver.getTitle().equals("STOVE")) {
                System.out.println("[loginHomePage] 로그인을 시도합니다.");

                // 계정 정보 불러오기
                Account account = getLoginAccount().get();
                String id = account.getId();
                String pw = account.getPw();

                // 아이디 입력
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_id"))).sendKeys(id);
//                System.out.println("[loginHomePage] 아이디 입력");

                // 패스워드 입력
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_pwd"))).sendKeys(pw);
//                System.out.println("[loginHomePage] 비밀번호 입력" + driver.getTitle());

                // 로그인
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#idLogin > div.row.grid.el-actions > button"))).click();
//                System.out.println("[loginHomePage] 로그인 버튼 클릭");
                Thread.sleep(1000); // 로그인 할 시간 기다리기
            } else {
                System.out.println("[loginHomePage] 이미 로그인 상태입니다.");
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#itemList")));
//            System.out.println("[loginHomePage] 검색 결과 페이지 로딩");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 문자열로 이루어진 가격을 float형 가격으로 변환한다.
     * @param str float형으로 변환하고자 하는 문자열, xx,xxx.x 혹은 xx,xxx 혹은 xxx와 같은 형식의 입력
     * @return str을 float형으로 변환하여 리턴한다.
     */
    private float toFloatNumber(String str) {
        float floatNumber = 0.0F;
        String splitedStr[] = str.split("\\."); // 점으로 구분
        if(splitedStr.length == 1) {
            // 가격이 5000, 6000과 같이 정수일 경우
            floatNumber += Float.parseFloat(splitedStr[0].replace(",",""));
        } else if(splitedStr.length == 2) {
            // 가격이 1.1, 1810.2와 같이 실수일 경우
            floatNumber += Float.parseFloat(splitedStr[0].replace(",",""));
            floatNumber += Float.parseFloat(splitedStr[1])/10;
        } else {
            // unexpected error
            for(String s : splitedStr) {
                System.out.println(s);
            }
            System.out.println("[ERROR] toFloatNumber : split error.");
        }
//        System.out.println("string to float : " + str + "to" +Float.toString(floatNumber));
        return floatNumber;
    }

}
