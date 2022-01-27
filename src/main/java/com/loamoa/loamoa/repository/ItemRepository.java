package com.loamoa.loamoa.repository;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.selenium.TaskSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

    private final TaskSelenium taskSelenium;

    @Autowired
    public ItemRepository(TaskSelenium taskSelenium) {
        this.taskSelenium = taskSelenium;
    }

    /**
     * 각인서 테스트
     */
    public void test() {
        String booknames[] = {"원한 각인서", "저주받은 인형 각인서", "예리한 둔기 각인서"};
        for(String s : booknames) {
            Item item = taskSelenium.getBookItemByName(s).get();
            System.out.println(item.toString());
        }
    }

    /**
     * 셀레니움의 크롬 드라이버를 중단시킨다.
     */
    public void cleanDriver() {
        taskSelenium.stopSelenium();
    }
}
