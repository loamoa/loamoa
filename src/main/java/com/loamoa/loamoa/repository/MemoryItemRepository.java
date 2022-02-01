package com.loamoa.loamoa.repository;

import com.loamoa.loamoa.domain.Item;
import com.loamoa.loamoa.domain.Skill;
import com.loamoa.loamoa.domain.SkillSet;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryItemRepository implements ItemRepository {
    // 지금 오류인진 몰라도 공식 홈페이지 포함 검색어에 [도화가] 이런식으로 직업명 들어가면 검색 안됨
    private static final Map<String, Item> store = new HashMap<>();
    private static final List<String> bookNames = new ArrayList<>(Arrays.asList("원한 각인서", "예리한 둔기 각인서", /*"[도화가] 만개 각인서",*/ "아드레날린 각인서"));
    private static final List<String> materialNames = new ArrayList<>(Arrays.asList("파괴석 결정", "파괴강석", "수호석 결정", "수호강석"));
    private static final List<Integer> classNos = new ArrayList<>();
    private static List<String> classNames = new ArrayList<>();
    private static final Map<Integer, SkillSet> skills = new LinkedHashMap<>();

    public MemoryItemRepository() {
        // 캐릭터 classNo 초기화
        for(int i=102; i<=602; i+=100) {
            for(int j=0; j<4; j++) {
                if(i == 402 && j == 3) break; // 암살자는 3클래스밖에 없음
                classNos.add(i+j);
                if(i == 602) break; // 도화가도 1클래스
            }
            if(i == 302 || i == 502) classNos.add(i+10); // 젠더락 처리
        }
        // classNo에 따른 클래스명 초기화 49-28+1 = 22개 클래스
        classNames = Arrays.asList("버서커",
                "디스트로이어",
                "워로드",
                "홀리나이트",
                "아르카나",
                "서머너",
                "바드",
                "소서리스",
                "배틀마스터",
                "인파이터",
                "기공사",
                "창술사",
                "스트라이커",
                "블레이드",
                "데모닉",
                "리퍼",
                "호크아이",
                "데빌헌터",
                "블래스터",
                "스카우터",
                "건슬링어",
                "도화가");
    }

    @Override
    public Item save(Item item) {
        store.put(item.getName(), item);
        return item;
    }

    @Override
    public Optional<Item> findItemByName(String itemName) {
        return Optional.ofNullable(store.get(itemName));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<Item>(store.values());
    }

    @Override
    public List<String> findAllItemName(int type) {
        List<String> arrayList = new ArrayList<>();
        if(type == 0) {
            arrayList.addAll(bookNames);
        } else if(type == 1) {
            arrayList.addAll(materialNames);
        }
        return arrayList;
    }

    public List<Integer> getClassNo() {
        return classNos;
    }

    public List<String> getClassName() {
        return classNames;
    }

    /**
     * 클래스명으로 클래스코드를 검색한다.
     * @param className 검색할 클래스명
     * @return null이 가능한 클래스코드
     */
    public Optional<Integer> getClassNoByClassName(String className) {
        Integer classNo = null;
        try {
            int idx = classNames.indexOf(className);
            if(idx != -1) classNo = classNos.get(idx);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(classNo);
    }

    /**
     * 클래스코드로 클래스명을 반환한다.
     * @param classNo 검색할 클래스코드
     * @return null이 가능한 클래스명
     */
    public Optional<String> getClassNameByClassNo(Integer classNo) {
        String className = null;
        try {
            int idx = classNos.indexOf(classNo);
            if(idx != -1) className= classNames.get(idx);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(className);
    }

    /**
     * DB의 classNo 직업코드를 가진 스킬셋에 skill을 추가한다.
     * @param classNo 대상 직업의 직업코드
     * @param skill 추가할 스킬
     */
    public void addCharacterSkill(Integer classNo, Skill skill) {
        try {
            if(skills.get(classNo) == null) skills.put(classNo, new SkillSet(classNo));
            skills.get(classNo).addSkill(skill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DB에서 전체 직업의 스킬셋을 가져온다.
     * @return 전체 직업의 스킬셋 리스트
     */
    public List<SkillSet> findAllClassSkill() {
        return new ArrayList<SkillSet>(skills.values());
    }

    /**
     * DB에서 특정 직업의 스킬셋을 가져온다.
     * @param classNo 스킬셋을 가져올 대상 직업코드
     * @return 대상 직업의 스킬셋
     */
    public SkillSet findClassSkill(Integer classNo) {
        return skills.get(classNo);
    }
}
