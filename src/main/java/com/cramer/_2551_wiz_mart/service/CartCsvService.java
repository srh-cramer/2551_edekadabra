package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.model.CartEntryData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CartCsvService {

    public void exportCart(List<CartEntryData> entries, File file) throws IOException {

        List<String> lines = new ArrayList<>();
        lines.add("productId,productName,quantity,unitPrice");

        for (CartEntryData entry : entries) {
            lines.add(
                    entry.id() + ","
                            + entry.name() + ","
                            + entry.quantity() + ","
                            + entry.unitPrice()
            );
        }

        Files.write(file.toPath(), lines);
    }

    public List<CartEntryData> importCart(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        List<CartEntryData> entries = new ArrayList<>();

        // Erste Zeile enthält die Überschriften, daher Start bei 1
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            // Für den Fall, dass die Zeile leer ist, überspringe sie
            if (line.isBlank()) {
                continue;
            }
            // Relativ simples Splitting, fehleranfällig aber für unsere Zwecke ok:
            String[] parts = line.split(",");

            // Kein Exception Handling für Parsing fehlerhafter Daten, für unsere Zwecke ok:
            int productId = Integer.parseInt(parts[0]);
            String productName = parts[1];
            int quantity = Integer.parseInt(parts[2]);
            double unitPrice = Double.parseDouble(parts[3]);

            CartEntryData entry = new CartEntryData(
                    productId,
                    productName,
                    quantity,
                    unitPrice
            );

            entries.add(entry);
        }

        return entries;
    }
}