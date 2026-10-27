package com.cramer._2551_wiz_mart.repository;

import com.cramer._2551_wiz_mart.model.Product;

import java.util.Optional;

public class ProductRepository extends Repository<Product>{

    public Optional<Product> findById(int id){
        return findAll().stream()
                .filter(item -> item.getId() == id)
                .findFirst();
    }
}
