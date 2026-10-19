package com.cramer._2551_wiz_mart.model;

public class Wand extends Product {

    private String woodType;
    private int magicPower;

    public Wand(int id, String name, double price, String description,
                int stock, String woodType, int magicPower) {

        super(id, name, price, Category.WAND, description, stock);

        this.woodType = woodType;
        this.magicPower = magicPower;
    }

    public String getWoodType() {
        return woodType;
    }

    public int getMagicPower() {
        return magicPower;
    }
}
