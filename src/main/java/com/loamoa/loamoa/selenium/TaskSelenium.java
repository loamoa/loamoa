package com.loamoa.loamoa.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.net.URL;
import java.util.Optional;

public class TaskSelenium {

    private WebDriver driver;
    private String baseUrl;
    WebDriverWait wait;

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
        options.addArguments("--no-sandbox"); // <이거 왜 쓰는진 모르겠음.. 찾아도 잘 안나옴>
        options.addArguments("disable-dev-shm-usage"); // shared memory 사용 중지
        options.addArguments("lang=ko"); // 서버 동작을 위해 언어 설정
        options.addArguments("window-size=1920x1080");
        options.addArguments("disable-gpu"); // 그래픽카드 가속 끄기

        driver = new ChromeDriver(options); // 옵션 포함한 드라이버 생성
        baseUrl = "https://lostark.game.onstove.com/"; // 로스트아크 공식 홈페이지 URL
        wait = new WebDriverWait(driver,5); // 드라이버가 최대 5초동안 기다림

        // Log
        System.out.println("[TaskSelenium] Driver Path = " + WEB_DRIVER_PATH);
        System.out.println("[TaskSelenium] baseURL = " + baseUrl);
    }

    public TaskSelenium() {
        // 기본 driverPath 입력
        this("selenium/chromedriver_97.0.4692.71/chromedriver");
    }

    /**
     * runSelenium()
     *
     */
    public void runSelenium() {
        // baseUrl 기준으로 브라우저를 실행
        driver.get(baseUrl);

        try {
            // 팝업창 끄기
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#modal-present > div > div > div.lui-modal__button > button.button--close"))).click();
            System.out.println("[runSelenium] Current Page1 : " + driver.getTitle());


            // 로그인 버튼 클릭
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#lostark-wrapper > header > div.header-menu > div > span.header-menu__login > a"))).click();
            System.out.println("[runSelenium] Current Page2 : " + driver.getTitle());

            // 아이디 입력
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_id"))).sendKeys("qjtjt4525@naver.com");
            System.out.println("[runSelenium] Current Page3 : " + driver.getTitle());

            // 패스워드 입력
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#user_pwd"))).sendKeys("tmddl990114!");
            System.out.println("[runSelenium] Current Page4 : " + driver.getTitle());

            // 로그인
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#idLogin > div.row.grid.el-actions > button"))).click();

            // 로그인 이후 홈페이지인지 확인
            wait.until(ExpectedConditions.titleIs("로스트아크"));
            System.out.println("[runSelenium] Current Page5 : " + driver.getTitle());



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 실행이 끝나면 연결 종료
            driver.close();
        }
    }

}
