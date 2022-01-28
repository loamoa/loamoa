package com.loamoa.loamoa.repository;

import com.loamoa.loamoa.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryItemRepository implements ItemRepository {
    // 지금 오류인진 몰라도 공식 홈페이지 포함 검색어에 [도화가] 이런식으로 직업명 들어가면 검색 안됨
    private static Map<String, Item> store = new HashMap<>();
    private static List<String> bookNames = new ArrayList<>(Arrays.asList("원한 각인서", "예리한 둔기 각인서", /*"[도화가] 만개 각인서",*/ "아드레날린 각인서"));
    private static List<String> materialNames = new ArrayList<>(Arrays.asList("파괴석 결정", "파괴강석", "수호석 결정", "수호강석"));

    @Override
    public Item save(Item item) {
        store.put(item.getName(), item);
        return item;
    }

    @Override
    public Optional<Item> findByName(String itemName) {
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
}
