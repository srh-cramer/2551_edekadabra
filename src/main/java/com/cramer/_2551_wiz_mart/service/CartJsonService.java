package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.model.CartEntryData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CartJsonService {

    public void exportCart(List<CartEntryData> entries, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, entries);
    }

    public List<CartEntryData> importCart(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<CartEntryData>> reference = new TypeReference<List<CartEntryData>>() {};
        return mapper.readValue(file, reference);
    }
}
