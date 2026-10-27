package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.data.ProductData;
import com.cramer._2551_wiz_mart.model.CartEntryData;
import com.cramer._2551_wiz_mart.model.CartItem;
import com.cramer._2551_wiz_mart.model.Product;
import com.cramer._2551_wiz_mart.model.ShoppingCart;
import com.cramer._2551_wiz_mart.repository.ProductRepository;


import java.util.ArrayList;
import java.util.List;

public class ShopService {

    private ProductRepository products;
    private ShoppingCart cart;

    public ShopService() {
        products = new ProductRepository();
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

    public List<CartEntryData> getCartEntryData(){
        List<CartEntryData> resultList = new ArrayList<>();
        //1. Hole Liste aller CartItems
        List<CartItem> cartItems = cart.getItems();
        //2. Für jedes CartItem im Liste:
            // Erstelle mir ein CartEntryData daraus
        for (CartItem item : cartItems){
            Product product = item.getProduct();
            CartEntryData cartEntryData = new CartEntryData(
                    product.getId(),
                    item.getQuantity(),
                    product.getName(),
                    product.getPrice()
            );
            resultList.add(cartEntryData);
        }
        //3. Gib mir Liste aller CartEntryData zurück
        return resultList;
    }

    public List<CartEntryData> getCartEntryDataWithStream(){
        //1. Hole Liste aller CartItems
        List<CartItem> cartItems = cart.getItems();
        //2. Mappe auf jedes Item ein neues CartEntryData-Objekt
        return cartItems.stream().map(item
                -> new CartEntryData(item.getProduct().getId(),
                item.getQuantity(),
                item.getProduct().getName(),
                item.getProduct().getPrice()))
                .toList();
    }
}
