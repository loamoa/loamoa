package com.loamoa.loamoa.domain;

public class MarketForm {
    Long firstCategory;
    Long secondCategory;
    Long characterClass;
    Long tier;
    Long grade;
    Long pageNo;
    Long sortType;
    String itemName;
    Boolean isInit;

    public MarketForm() {
        this.firstCategory = 0L; // 전체 카테고리
        this.secondCategory = 0L; // 전체 카테고리
        this.characterClass = 0L; // 전체 캐릭터 클래스
        this.tier = 0L; // 전체 티어
        this.grade = 99L; // 전체 등급
        this.itemName = ""; // 모든 아이템 명
        this.pageNo = 1L; // 1페이지의 결과
        this.isInit = false; // 초기 페이지 X
        this.sortType = 7L; // 최저가 기준 오른차순 정렬
    }

    public Long getFirstCategory() {
        return firstCategory;
    }

    public String getFirstCategoryToString() {
        return firstCategory.toString();
    }

    public void setFirstCategory(Long firstCategory) {
        this.firstCategory = firstCategory;
    }

    public Long getSecondCategory() {
        return secondCategory;
    }

    public String getSecondCategoryToString() {
        return secondCategory.toString();
    }

    public void setSecondCategory(Long secondCategory) {
        this.secondCategory = secondCategory;
    }

    public Long getCharacterClass() {
        return characterClass;
    }

    public String getCharacterClassToString() {
        // characterClass는 공백일 때 전체 직업을 선택함.
        if(characterClass.longValue() == 0) return "";
        else return characterClass.toString();
    }

    public void setCharacterClass(Long characterClass) {
        this.characterClass = characterClass;
    }

    public Long getTier() {
        return tier;
    }

    public String getTierToString() {
        return tier.toString();
    }

    public void setTier(Long tier) {
        this.tier = tier;
    }

    public Long getGrade() {
        return grade;
    }

    public String getGradeToString() {
        return grade.toString();
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public String getPageNoToString() {
        return pageNo.toString();
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public Long getSortType() {
        return sortType;
    }

    public String getSortTypeToString() {
        return sortType.toString();
    }

    public void setSortType(Long sortType) {
        this.sortType = sortType;
    }

    public Boolean getInit() {
        return isInit;
    }

    public String getInitToString() {
        return isInit.toString();
    }

    public void setInit(Boolean init) {
        isInit = init;
    }

    @Override
    public String toString() {
        return
                "firstCategory=" + getFirstCategoryToString() +
                "&secondCategory=" + getSecondCategoryToString() +
                "&characterClass=" + getCharacterClassToString() +
                "&tier=" + getTierToString() +
                "&grade=" + getGradeToString() +
                "&itemName=" + getItemName() +
                "&pageNo=" + getPageNoToString() +
                "&isInit=" + getInit() +
                "&sortType=" + getSortTypeToString();
    }
}
