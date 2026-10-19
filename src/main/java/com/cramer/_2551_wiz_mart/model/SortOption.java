package com.cramer._2551_wiz_mart.model;

public enum SortOption {

    NAME("Name"),
    PRICE_ASCENDING("Price: low to high"),
    PRICE_DESCENDING("Price: high to low");

    private final String label;

    SortOption(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString(){
        return label;
    }
}
