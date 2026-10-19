package com.cramer._2551_wiz_mart.repository;

import java.util.ArrayList;
import java.util.List;

public class Repository<T> {
    private List<T> items;

    public Repository() {
        this.items = new ArrayList<>();
    }

    public void add(T item){
        items.add(item);
    }

    public void remove(T item){
        items.remove(item);
    }

    public List<T> findAll(){
        return items;
    }
}
