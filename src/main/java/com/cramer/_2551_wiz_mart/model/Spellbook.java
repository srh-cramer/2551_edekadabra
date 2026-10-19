package com.cramer._2551_wiz_mart.model;

public class Spellbook extends Product {

    private String school;
    private int spellCount;

    public Spellbook(int id, String name, double price, String description,
                     int stock, String school, int spellCount) {

        super(id, name, price, Category.SPELLBOOK, description, stock);

        this.school = school;
        this.spellCount = spellCount;
    }

    public String getSchool() {
        return school;
    }

    public int getSpellCount() {
        return spellCount;
    }
}
