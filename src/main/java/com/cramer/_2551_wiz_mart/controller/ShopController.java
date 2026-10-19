package com.cramer._2551_wiz_mart.controller;

import com.cramer._2551_wiz_mart.model.CartItem;
import com.cramer._2551_wiz_mart.model.Category;
import com.cramer._2551_wiz_mart.model.Product;
import com.cramer._2551_wiz_mart.model.SortOption;
import com.cramer._2551_wiz_mart.service.ShopService;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ShopController {
    @FXML
    private FlowPane productPane;

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    private Label totalLabel;

    @FXML
    private Label cartCountLabel;

    @FXML
    private Label productCountLabel;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<SortOption> sortComboBox;

    private ShopService shopService;

    @FXML
    public void initialize() {
        shopService = new ShopService();
        shopService.getCart().addObserver(() -> updateCartView());

        setupCategories();
        setupSorting();

        categoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            displayProducts();
        });
        sortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            displayProducts();
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            displayProducts();
        });

        displayProducts();
    }

    // Füge dem Kategorie-Dropdown die Einträge hinzu
    private void setupCategories() {
        categoryComboBox.getItems().add("All");

        for (Category category : Category.values()){
            categoryComboBox.getItems().add(category.name());
        }

        categoryComboBox.setValue("All");
    }

    // Füge dem Sortierungs-Dropdown die Einträge hinzu
    private void setupSorting() {
        //sortComboBox.getItems().add(SortOption.NAME);

        for (SortOption sortOption : SortOption.values()){
            sortComboBox.getItems().add(sortOption);
        }

        sortComboBox.setValue(SortOption.NAME);
    }

    // Zeige nur die Produkte an, nach denen gefiltert/gesucht wurde
    // Standard: Zeige alle an
    // TODO: Sortierung berücksichtigen
    private void displayProducts() {

        productPane.getChildren().clear();

        String searchText = searchField.getText().toLowerCase();
        String selectedCategory = categoryComboBox.getValue();
        SortOption selectedSort = sortComboBox.getValue();

        List<Product> products = shopService.getProducts();
        List<Product> filterProducts = new ArrayList<>(); // Liste, die gefilterte/gesuchte Produkte enthalten soll

        for (Product product : products) {

            // Suche
            if (!searchText.isEmpty()
                    && !product.getName().toLowerCase().contains(searchText)) {
                continue;
            }

            // Filtern
            String currentProductCategory = product.getCategory().toString();
            if (!selectedCategory.equals("All")
                    && !currentProductCategory.equals(selectedCategory)) {
                continue;
            }

            filterProducts.add(product); // füge gesuchtes/gefiltertes Produkt zu Anzeigeliste hinzu

        }

        // Sortiere die Anzeigeliste:
        if (selectedSort == SortOption.PRICE_ASCENDING) {
            filterProducts.sort((product1, product2) ->
                    Double.compare(product1.getPrice(), product2.getPrice()));
        }
        else if (selectedSort == SortOption.PRICE_DESCENDING){
            filterProducts.sort((product1, product2) ->
                    Double.compare(product2.getPrice(), product1.getPrice()));
        }
        else if (selectedSort == SortOption.NAME){
            filterProducts.sort((product1, product2) ->
                    product1.getName().compareTo(product2.getName()));
        }

        // Platziere alle Produkte aus Anzeigenliste in GUI:
        for (Product p : filterProducts) {
            productPane.getChildren().add(createProductCard(p));
        }

        productCountLabel.setText(
                "Products: " + productPane.getChildren().size()
        );
    }

    // Erstelle eine VBox, die ein einzelnes Produkt anzeigt
    private VBox createProductCard(Product product) {

        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setPrefWidth(220);

        Label nameLabel = new Label(product.getName());
        Label categoryLabel = new Label(product.getCategory().toString());
        Label priceLabel = new Label(
                String.format("%.2f Gold", product.getPrice())
        );
        Label descriptionLabel = new Label(product.getDescription());

        descriptionLabel.setWrapText(true);

        Label stockLabel = new Label(
                "Stock: " + product.getStock()
        );

        Button addButton = new Button("Add to cart");

        addButton.setOnAction(event -> {
            shopService.addToCart(product);
        });

        card.getChildren().addAll(
                nameLabel,
                categoryLabel,
                priceLabel,
                descriptionLabel,
                stockLabel,
                addButton
        );

        return card;
    }

    private void updateCartView() {

        cartListView.getItems().clear();

        cartListView.getItems().addAll(
                shopService.getCart().getItems()
        );

        totalLabel.setText(
                String.format(
                        "Total: %.2f Gold",
                        shopService.getCart().getTotalPrice()
                )
        );

        cartCountLabel.setText(
                "Items: " + shopService.getCart().getItemCount()
        );
    }

    @FXML
    private void handleSearch(){
        displayProducts();
    }

    @FXML
    private void handleClearCart() {
        shopService.clearCart();
    }

    @FXML
    private void handleCheckout() {

        if (shopService.getCart().getItems().isEmpty()) {
            showMessage(
                    "Empty Cart",
                    "Your cart is empty."
            );
            return;
        }

        showMessage(
                "Checkout",
                "Order completed!"
        );

        shopService.clearCart();
    }

    private void showMessage(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
