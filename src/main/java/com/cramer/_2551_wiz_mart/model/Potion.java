package com.cramer._2551_wiz_mart.model;

public class Potion extends Product {
    private String effect;
    private int strength;

    public Potion(int id, String name, double price, String description,
                  int stock, String effect, int strength) {

        super(id, name, price, Category.POTION, description, stock);

        this.effect = effect;
        this.strength = strength;
    }

    public String getEffect() {
        return effect;
    }

    public int getStrength() {
        return strength;
    }
}
