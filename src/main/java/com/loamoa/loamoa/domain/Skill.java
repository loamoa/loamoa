package com.loamoa.loamoa.domain;

public class Skill {
    Integer skillCode;
    String skillName;

    public Skill(Integer skillCode, String skillName) {
        this.skillCode = skillCode;
        this.skillName = skillName;
    }

    public Integer getSkillCode() {
        return skillCode;
    }

    public void setSkillCode(Integer skillCode) {
        this.skillCode = skillCode;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
