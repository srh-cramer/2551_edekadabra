package com.cramer._2551_wiz_mart.service;

import com.cramer._2551_wiz_mart.model.CartEntryData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CartCsvService {

    public void exportCart(List<CartEntryData> entries, File file) throws IOException {
        List<String> lines = new ArrayList<>();
        // 1. Header-Zeile erstellen
        String headerLine = "productId, name, quantity, unitPrice";
        lines.add(headerLine);
        // 2. Für jeden Eintrag eine Zeile zusammenbauen
        for (CartEntryData entry : entries){
            String line = entry.id() + ", " +
                    entry.name() + ", " +
                    entry.quantity() + ", " +
                    entry.unitPrice();
            lines.add(line);
        }
        // 3. Header und alle Zeilen in die Datei schreiben
        Files.write(file.toPath(), lines);
    }

    public List<CartEntryData> importCart(File file) throws IOException{
        List<CartEntryData> entries = new ArrayList<>();

        // Datei lesen und in Liste aus Zeilen speichern
        List<String> lines = Files.readAllLines(file.toPath());

        // Für jeden Eintrag (außer ersten, daher i=1) in Zeilen:
        for (int i=1; i < lines.size(); i++) {
            // Zeilen in Parts aufteilen: "3, Cloak, 1, 19.99" -> {"3", "Cloak", "1", "19.99"}
            String[] lineParts = lines.get(i).split(",");
            // Erstelle CartEntryData-Objekt aus Zeilenparts
            int id = Integer.parseInt(lineParts[0].trim());
            String name = lineParts[1].trim();
            int quantity = Integer.parseInt(lineParts[2].trim());
            double price = Double.parseDouble(lineParts[3].trim());
            CartEntryData entry = new CartEntryData(id, quantity, name, price);
            entries.add(entry);
        }
        // Gib alle CartEntryData-Objekte in List zurück
        return entries;
    }
}
