package com.cramer._2551_wiz_mart.data;

import com.cramer._2551_wiz_mart.model.Product;
import com.cramer._2551_wiz_mart.model.Potion;
import com.cramer._2551_wiz_mart.model.Cloak;
import com.cramer._2551_wiz_mart.model.Wand;
import com.cramer._2551_wiz_mart.model.Spellbook;

import java.util.ArrayList;
import java.util.List;

public class ProductData {

    public static List<Product> createProducts() {

        List<Product> products = new ArrayList<>();

        // Potions
        products.add(new Potion(
                1,
                "Healing Potion",
                12.50,
                "Restores 50 health points.",
                20,
                "Healing",
                50
        ));

        products.add(new Potion(
                2,
                "Mana Potion",
                15.00,
                "Restores magical energy.",
                15,
                "Mana",
                40
        ));

        products.add(new Potion(
                3,
                "Invisibility Potion",
                35.00,
                "Makes the drinker invisible for a short time.",
                8,
                "Invisibility",
                10
        ));

        // Cloaks
        products.add(new Cloak(
                4,
                "Cloak of Shadows",
                149.99,
                "A dark cloak that helps its wearer remain unseen.",
                5,
                "Enchanted Silk",
                80
        ));

        products.add(new Cloak(
                5,
                "Flying Cloak",
                199.99,
                "Allows the wearer to glide through the air.",
                3,
                "Moonwoven Fabric",
                60
        ));

        products.add(new Cloak(
                6,
                "Wizard's Robe",
                79.99,
                "A traditional robe for everyday magical activities.",
                12,
                "Dragon Wool",
                40
        ));

        // Wands
        products.add(new Wand(
                7,
                "Oak Wand",
                45.00,
                "A reliable wand suitable for beginner wizards.",
                10,
                "Oak",
                40
        ));

        products.add(new Wand(
                8,
                "Phoenix Wand",
                89.99,
                "A powerful wand containing a phoenix feather core.",
                5,
                "Phoenix Wood",
                90
        ));

        products.add(new Wand(
                9,
                "Elder Wand",
                249.99,
                "An extremely powerful and rare magical wand.",
                1,
                "Elder",
                100
        ));

        // Spellbooks
        products.add(new Spellbook(
                10,
                "Beginner's Spells",
                29.99,
                "A collection of basic spells for aspiring wizards.",
                15,
                "Beginner Magic",
                20
        ));

        products.add(new Spellbook(
                11,
                "Book of Fire",
                59.99,
                "A collection of powerful fire spells.",
                7,
                "Elemental Magic",
                25
        ));

        products.add(new Spellbook(
                12,
                "Advanced Transfiguration",
                99.99,
                "Advanced spells for transforming objects.",
                4,
                "Transfiguration",
                35
        ));

        return products;
    }
}