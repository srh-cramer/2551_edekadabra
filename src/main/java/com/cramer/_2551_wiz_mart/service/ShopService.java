package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.data.ProductData;
import com.cramer._2551_wiz_mart.model.Product;
import com.cramer._2551_wiz_mart.model.ShoppingCart;
import com.cramer._2551_wiz_mart.repository.Repository;


import java.util.List;

public class ShopService {

    private Repository<Product> products;
    private ShoppingCart cart;

    public ShopService() {
        products = new Repository<>();
        for (Product product : ProductData.createProducts()){
            products.add(product);
        }
        cart = new ShoppingCart();
    }

    public List<Product> getProducts() {
        return products.findAll();
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void addToCart(Product product) {
        cart.addProduct(product);
    }

    public void removeFromCart(Product product) {
        cart.removeProduct(product);
    }

    public void clearCart() {
        cart.clear();
    }
}
