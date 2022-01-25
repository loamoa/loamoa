package com.loamoa.loamoa.domain;
public class Item {
    private String name;
    private String upgrade;
    private String parts;
    private String level;
    private int quality;
    private String icon;
    private String engrave1_name;
    private String engrave1_value;
    private String engrave2_name;
    private String engrave2_value;

    /*
    ** Getter and Setter
     */
    public String getEngrave1_value() {
        return engrave1_value;
    }
    public void setEngrave1_value(String engrave1_value) {
        this.engrave1_value = engrave1_value;
    }
    public String getEngrave2_value() {
        return engrave2_value;
    }
    public void setEngrave2_value(String engrave2_value) {
        this.engrave2_value = engrave2_value;
    }
    public String getEngrave1_name() {
        return engrave1_name;
    }
    public void setEngrave1_name(String engrave1_name) {
        this.engrave1_name = engrave1_name;
    }
    public String getEngrave2_name() {
        return engrave2_name;
    }
    public void setEngrave2_name(String engrave2_name) {
        this.engrave2_name = engrave2_name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUpgrade() {
        return upgrade;
    }
    public void setUpgrade(String upgrade) {
        this.upgrade = upgrade;
    }
    public String getParts() {
        return parts;
    }
    public void setParts(String parts) {
        this.parts = parts;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public int getQuality() {
        return quality;
    }
    public void setQuality(int quallity) {
        this.quality = quallity;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
