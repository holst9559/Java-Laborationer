package org.example.Laboration3.service;

import org.example.Laboration3.entities.Category;
import org.example.Laboration3.entities.Product;
import org.example.Laboration3.entities.ProductRecord;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/*Utöka Warehouse med ytterligare funktioner för:

•Hämta alla produkter med max rating, skapade denna månad och sorterat på datum med nyast först.
*/

public class Warehouse {

    private final List<Product> products = new ArrayList<>();
    private final int maxRating = 5;


    public ProductRecord addNewProduct(ProductRecord product) {
        if (product.name() == null || product.name().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        } else if (product.rating() > maxRating || product.rating() < 0) {
            throw new IllegalArgumentException("Rating cannot be higher than 5");
        }

        Product newProduct = new Product(product);
        products.add(newProduct);


        return newProduct.toRecord();
    }

    public ProductRecord updateProduct(ProductRecord productUpdate) {
        return products.stream().filter(product -> product.getId().equals(productUpdate.id())).findFirst()
                .map(product -> {
                    product.setName(productUpdate.name());
                    product.setCategory(productUpdate.category());
                    product.setRating(productUpdate.rating());
                    product.setUpdatedAt(LocalDate.now());
                    return product.toRecord();
                }).orElseThrow(() -> new IllegalArgumentException("Product ID does not exist"));
    }

    public List<ProductRecord> getAllProducts() {
        return products.stream()
                .map(Product::toRecord)
                .toList();
    }

    public ProductRecord getProductById(String productId) {
        return products.stream().filter(product -> product.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product ID does not exist"))
                .toRecord();
    }

    public List<ProductRecord> getProductsByCategory(Category category) {
        return products.stream().filter(product -> product.getCategory().equals(category))
                .sorted(Comparator.comparing(Product::getName))
                .map(Product::toRecord)
                .toList();
    }

    public List<ProductRecord> getProductsAfterDate(LocalDate localDate) {
        return products.stream().filter(product -> product.getCreatedAt().isAfter(localDate))
                .map(Product::toRecord)
                .toList();
    }

    public List<ProductRecord> getUpdatedProducts() {
        return products.stream().filter(product -> !product.getUpdatedAt().equals(product.getCreatedAt()))
                .map(Product::toRecord)
                .toList();
    }

    public List<Category> getPopulatedCategories() {
        return products.stream().collect(Collectors.groupingBy(Product::getCategory))
                .keySet()
                .stream()
                .toList();

    }

    public int getNumberOfProductsInCategory(Category category) {
        return products.stream().map(Product::getCategory)
                .filter(productCategory -> productCategory.equals(category))
                .toList()
                .size();
    }

    public Map<Character, Long> getProductMap() {
        return products.stream().collect(Collectors.groupingBy(product -> product.getName().charAt(0),
                Collectors.counting()));
    }

    public List<ProductRecord> getMaxRatingLastMonth() {
        return products.stream().filter(product -> product.getCreatedAt()
                        .isAfter(LocalDate.now().minusMonths(1)))
                .filter(product -> product.getRating() == maxRating)
                .sorted(Comparator.comparing(Product::getCreatedAt).reversed())
                .map(Product::toRecord)
                .toList();
    }
}
