package com.alisure.weixin.template;

public class TemplateData {
    private String value;
    private String color;

    public TemplateData() {

    }

    public TemplateData(String value) {
        this.value = value;
        this.color = "#000000";
    }

    public TemplateData(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}  