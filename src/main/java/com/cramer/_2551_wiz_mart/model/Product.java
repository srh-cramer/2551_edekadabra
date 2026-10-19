package com.cramer._2551_wiz_mart.model;

public abstract class Product {
    private int id;
    private String name;
    private double price;
    private Category category;
    private String description;
    private int stock;

    public Product(int id, String name, double price, Category category,
                   String description, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return name + " - " + price + " Gold";
    }

}
