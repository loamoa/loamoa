package com.loamoa.loamoa.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class ItemPriceDriver {
    //웹 드라이버
    private final WebDriver driver;
    private final String baseUrl; // 접속할 URL
    WebDriverWait wait;     // 페이지 로딩을 기다리는 동작을 위함

    public ItemPriceDriver() {
        Optional<String> filePath;  // chrome driver path
        ChromeOptions options;  // chrome driver options

        // chrome driver 존재 여부 확인
        filePath = checkDriver();
        // 시스템 속성
        setProperties(filePath);
        // driver options
        options = setChromeOptions();

        driver = new ChromeDriver(options); // 옵션 포함한 드라이버 생성
        wait = new WebDriverWait(driver,10);
        baseUrl = "https://lostark.game.onstove.com/"; // 로스트아크 공식 홈페이지 URL
    }

    /**
     * Chrome driver가 정해진 경로에 위치 하는지 확인
     */
    private Optional<String> checkDriver() {
        URL resource;
        Optional<String> filePath;
        try {
            resource = getClass().getClassLoader().getResource("selenium/chromedriver");
            filePath = Optional.ofNullable(resource.getFile());
            return filePath;
        } catch (NullPointerException e) {
            System.out.println("src/main/resources/selenium 에 chromedriver가 존재 하지 않습니다.");
            System.exit(1);
            return null;
        }
    }

    /**
     * Chrome driver의 옵션을 설정
     */
    private ChromeOptions setChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("ignoreProtectedModeSettings", true); // 보호모드를 무시하는 설정
//        options.addArguments("--headless"); // 브라우저 창 없이 실행
//        options.addArguments("--no-sandbox"); // 크롬 보안 기능 sandbox를 사용하지 않음
//        options.addArguments("disable-gpu"); // GPU 사용 X
//        options.addArguments("--window-size=1920,1080"); // 기본 화면 크기 지정
        options.addArguments("lang=ko"); // 서버 동작을 위해 언어 설정

        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/97.0.4692.71 Safari/537.36"); // Headless임을 숨기기 위한 수정 값


        return options;
    }

    /**
     * Chrome driver의 속성을 설정
     * @param filePath Chome driver의 위치
     */
    private void setProperties(Optional<String> filePath) {
        String WEB_DRIVER_ID = "webdriver.chrome.driver";
        String WEB_DRIVER_PATH = filePath.get();
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
    }

    /**
     * base URL을 통해 Chrome Driver 실행
     */
    public void runDriver(){
        driver.get(baseUrl);
    }

    /**
     * 메인 페이지 이외의 팝업창을 닫음 (현재 LostArk의 경우 Popup이 modal 형태로 존재하여 작동하지 않음)
     */
    public void closePopUp() {
        String main = driver.getWindowHandle(); // 메인 화면의 정보를 저장

        // 모든 화면의 정보를 순회하며 만약 메인 화면이 아닐 경우 닫음
        for (String handle : driver.getWindowHandles()) {
            if( !handle.equals(main)) {
                driver.switchTo().window(handle).close();
            }
        }
        // 화면의 위치를 다시 메인으로 변경
        driver.switchTo().window(main);
    }

    public void clickByCssSelector(String selector) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector))).click();
    }

    public void sendKeyByCssSelector(String selector, String key) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(selector))).sendKeys(key);
    }

    public List<WebElement> getElementsByCssSelector(String selector) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className(selector)));
        return driver.findElements(By.className(selector));
    }

    /**
     * ID와 PW를 바탕으로 LostArk 로그인을 진행
     * @param id 로그인 할 ID
     * @param pw 로그인 할 Password
     */
    public void login(String id, String pw) {
        try {
            // 팝업창 끄기
            clickByCssSelector("#modal-present > div > div > div.lui-modal__button > button.button--close");
            System.out.println("[ItemPriceDriver] Close Popup");

            // 로그인 버튼 클릭
            clickByCssSelector("#lostark-wrapper > header > div.header-menu > div > span.header-menu__login > a");
            System.out.println("[ItemPriceDriver] Click Login Button");

            // 아이디 입력
            sendKeyByCssSelector("#user_id", id);
            System.out.println("[ItemPriceDriver] Input ID");

            // 패스워드 입력
            sendKeyByCssSelector("#user_pwd", pw);
            System.out.println("[ItemPriceDriver] Input Password");

            // 로그인
            clickByCssSelector("#idLogin > div.row.grid.el-actions > button");

            // 로그인 이후 홈페이지인지 확인
            wait.until(ExpectedConditions.titleIs("로스트아크"));
            System.out.println("[ItemPriceDriver] Current Page : " + driver.getTitle());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
