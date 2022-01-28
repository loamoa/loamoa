package com.loamoa.loamoa.service;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.MarketForm;
import com.loamoa.loamoa.repository.MemoryItemRepository;
import com.loamoa.loamoa.selenium.TaskSelenium;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final MemoryItemRepository itemRepository;
    private final TaskSelenium taskSelenium;

    @Autowired
    public ItemService(MemoryItemRepository itemRepository, TaskSelenium taskSelenium) {
        this.itemRepository = itemRepository;
        this.taskSelenium = taskSelenium;
    }

    /**
     * 전체 아이템 가격 조회
     * @return 조회한 아이템 리스트
     */
    public List<Item> findItems() {
        List<Item> itemList = itemRepository.findAll();
        for(Item i : itemList) {
            System.out.println("[ItemService] "+i.toString());
        }
        return itemList;
    }

    /**
     * Scheduled - 원하는 간격에 메소드를 실행
     * cron = ( 초(0-59) | 분(0-59) | 시간(0-23) | 일(1-31) | 월(1-12) | 요일(0-7) )
     */
    @Scheduled(cron = "0 0 7 * * *") // 매일 오전 7시 실행
    public void refreshItems() {
        try {
            int type;
            int MAX_TYPE = 2;
            for (type = 0; type < MAX_TYPE; type++) {
                // 저장소에서 type별로 검색할 아이템명을 모두 가져온다.
                List<String> itemNames = itemRepository.findAllItemName(type);
                System.out.println(type + itemNames.toString());
                // 각각의 아이템명에 따라서 검색 결과를 업데이트한다.
                for (String name : itemNames) {
                    // 여기는 검색별로 상수 설정하는건데 나중에 메소드로 빼거나 하면 될듯?
                    MarketForm form = new MarketForm();
                    if (type == 0) {
                        // 각인서 폼으로 설정
                        form.setFirstCategory(40000L); // 각인서
                        form.setSecondCategory(0L); // 각인서-전체
                        form.setGrade(4L); // 전설 등급
                    } else if (type == 1) {
                        // 재료 폼으로 설정
                        form.setFirstCategory(50000L); // 강화 재료
                        form.setSecondCategory(0L); // 강화 재료-전체
                        form.setTier(3L); // 3티어 강화재료
                    }
                    form.setItemName(name);
                    itemRepository.save(taskSelenium.getItem(form).get());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}