package com.cramer._2551_wiz_mart.model;

public record CartEntryData(
        int id,
        String name,
        int quantity,
        double unitPrice
) {}
