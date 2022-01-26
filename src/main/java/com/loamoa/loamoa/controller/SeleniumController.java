package com.loamoa.loamoa.controller;

import com.loamoa.loamoa.selenium.TaskSelenium;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeleniumController {

    @GetMapping("/management/selenium")
    public void selenium() throws InterruptedException {
        TaskSelenium taskSelenium = new TaskSelenium();
        taskSelenium.runSelenium();
    }
}

