package com.cramer._2551_wiz_mart.model;

public class Cloak extends Product {

    private String material;
    private int protectionLevel;

    public Cloak(int id, String name, double price, String description,
                 int stock, String material, int protectionLevel) {

        super(id, name, price, Category.CLOAK, description, stock);

        this.material = material;
        this.protectionLevel = protectionLevel;
    }

    public String getMaterial() {
        return material;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }
}
