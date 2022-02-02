package com.loamoa.loamoa.service;

import com.loamoa.loamoa.domain.Skill;
import com.loamoa.loamoa.domain.SkillSet;
import com.loamoa.loamoa.repository.MemoryItemRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.loamoa.loamoa.utility.Utills.sortMapByKey;

@Service
public class AuctionService {

    private final MemoryItemRepository itemRepository;

    @Autowired
    public AuctionService(MemoryItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<SkillSet> findAllSkillSet() {
        return itemRepository.findAllClassSkill();
    }

//    public SkillSet findSkillSetByClassName(String className) {
//        try {
//            Integer classNo = itemRepository.getClassNoByClassName(className).get();
//
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//        }
////        return new SkillSet();
//    }

    /**
     * 데이터베이스에 직업별 스킬을 등록한다.
     */
    public void addCharacterSkills() {
        try {
            // 검색할 캐릭터의 직업명, 직업코드 가져오기
            List<String> names = itemRepository.getClassName();
            List<Integer> classNo = itemRepository.getClassNo();

            for(int i=0; i<names.size(); i++) {
                Map<Integer, String> skills = getClassSkillCode(classNo.get(i)); // 각 직업별로 스킬들을 크롤링

                // 크롤링한 스킬들을 DB에 추가
                for(Map.Entry e : skills.entrySet()) {
                    // classNo의 스킬셋에 Skill을 추가
                    itemRepository.addCharacterSkill(classNo.get(i), new Skill((Integer) e.getKey(), (String) e.getValue()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 캐릭터(characterCode) 별 스킬을 스킬코드-스킬명 쌍을 가진 HashMap으로 반환합니다.
     * @param classNo 검색하려는 캐릭터 코드
     * @return 스킬코드-스킬명 쌍의 해시맵
     */
    public HashMap<Integer, String> getClassSkillCode(int classNo) {
        HashMap<Integer, String> result = new LinkedHashMap<>(); // 결과 맵
        try {
            // 로스트아크 인벤 스킬DB 사이트에서 크롤링 실행
            String baseUrl = "https://lostark.inven.co.kr/dataninfo/skill/?reqjob=" + Integer.toString(classNo); // 뒤에 "/" 추가하면 도화가만 나오는 오류 생김
            Document doc = Jsoup.connect(baseUrl).get();

            Elements codeElement = doc.select("td.txt_left > a.name"); // 스킬코드 크롤링
            Elements nameElement = doc.select("td.txt_left > a.name > p"); // 스킬명 크롤링

            // 스킬코드-스킬명 매칭 후 맵에 등록
            for(int i=0; i< nameElement.size(); i++) {
                String code = codeElement.get(i).attr("data-lostark-skill-code");
                String name = nameElement.get(i).text();
                result.put(Integer.parseInt(code.substring(0,5)), name);
            }
        } catch (IOException e) {
            System.out.println("[getCharacterSkillCode] doc get error " + e);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 정렬 후 리턴
        return sortMapByKey(result);
    }
}
