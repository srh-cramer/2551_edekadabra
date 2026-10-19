package com.cramer._2551_wiz_mart.model;

import java.util.ArrayList;
import java.util.List;

// Repräsentiert den Warenkorb, in dem sich CartItems befinden
public class ShoppingCart {

    private List<CartItem> items;
    //Liste der Beobachter:
    private List<CartObserver> observers;

    public ShoppingCart() {
        items = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void addProduct(Product product) {

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.increaseQuantity();
                notifyObservers();
                return;
            }
        }

        items.add(new CartItem(product, 1));
        notifyObservers();
    }

    public void removeProduct(Product product) {

        items.removeIf(item ->
                item.getProduct().getId() == product.getId()
        );
        notifyObservers();
    }

    public void clear() {
        items.clear();
        notifyObservers();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {

        double total = 0;

        for (CartItem item : items) {
            total += item.getTotalPrice();
        }

        return total;
    }

    public int getItemCount() {

        int count = 0;

        for (CartItem item : items) {
            count += item.getQuantity();
        }

        return count;
    }

    public void addObserver(CartObserver observer){
        observers.add(observer);
    }

    public void removeObserver(CartObserver observer){
        observers.remove(observer);
    }

    private void notifyObservers(){
        for (CartObserver observer: observers){
            observer.cartChanged();
        }
    }
}
