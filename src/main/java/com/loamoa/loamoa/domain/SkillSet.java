package com.loamoa.loamoa.domain;

import java.util.ArrayList;
import java.util.List;

public class SkillSet {
    private List<Skill> skillSet = new ArrayList<>();
    private Integer classNo;

    public SkillSet(Integer classNo) {
        this.classNo = classNo;
    }

    public List<Skill> getSkillSet() {
        return skillSet;
    }

    public Skill addSkill(Skill skill) {
        skillSet.add(skill);
        return skill;
    }

    public Integer getClassNo() {
        return classNo;
    }

    public void setClassNo(Integer classNo) {
        this.classNo = classNo;
    }
}
