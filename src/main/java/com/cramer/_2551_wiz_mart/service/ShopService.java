package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.data.ProductData;
import com.cramer._2551_wiz_mart.model.CartEntryData;
import com.cramer._2551_wiz_mart.model.Product;
import com.cramer._2551_wiz_mart.model.ShoppingCart;
import com.cramer._2551_wiz_mart.repository.ProductRepository;
import com.cramer._2551_wiz_mart.repository.Repository;


import java.util.List;
import java.util.Optional;

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

    // Wandle Warenkorb in Liste aus Records um
    // Benutze dafür einen Stream und map()
    public List<CartEntryData> getCartEntryData() {
        return cart.getItems().stream()
                .map(item -> new CartEntryData(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice()
                ))
                .toList();
    }

    // Wandle Records in Produkte um und füge diese dem Warenkorb hinzu
    public void importCartEntries(List<CartEntryData> entries){
        for (CartEntryData entry : entries){
            Optional<Product> product = products.findById(entry.id());
            if (product.isPresent()){
                for (int i=0; i < entry.quantity(); i++){
                    cart.addProduct(product.get());
                }
            }
        }
    }
}
