package com.cramer._2551_wiz_mart.model;

public record CartEntryData(
        int id,
        int quantity,
        String name,
        double unitPrice
) {}
