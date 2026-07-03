package com.example.ashraftraders.data.model;


public class SummaryModel {

    private int icon;
    private String title;
    private String value;
    private String growth;

    public SummaryModel(int icon,
                        String title,
                        String value,
                        String growth) {

        this.icon = icon;
        this.title = title;
        this.value = value;
        this.growth = growth;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getGrowth() {
        return growth;
    }

}
