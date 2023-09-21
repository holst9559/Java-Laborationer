package org.example;

import static org.example.Laboration3.entities.Category.*;

import org.example.Laboration3.entities.ProductRecord;
import org.example.Laboration3.entities.Product;

import org.example.Laboration3.service.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    private Warehouse warehouse;
    private LocalDate now;

    @BeforeEach
    void setUp(){
        warehouse = new Warehouse();
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        now = LocalDate.now(fixedClock);

    }

    @Test
    void shouldAddNewProduct() {
        Product product = new Product("Pilsnermalt", MALT, 4);
        warehouse.addNewProduct(product.toRecord());

        assertNotNull(product.getId());
        assertEquals("Pilsnermalt", product.getName());
        assertEquals(MALT, product.getCategory());
        assertEquals(4, product.getRating());
        assertEquals(now, product.getCreatedAt());
        assertEquals(now, product.getUpdatedAt());
    }

    @Test
    void shouldThrowAddNewProductException() {
        Product product1 = new Product("", MALT, 4);
        Product product2 = new Product("Pilsnermalt", MALT, 6);

        assertThrows(IllegalArgumentException.class, () -> warehouse.addNewProduct(product1.toRecord()));
        assertThrows(IllegalArgumentException.class, () -> warehouse.addNewProduct(product2.toRecord()));
    }


    @Test
    void shouldUpdateProduct() {
        Product mockProduct = new Product("Pilsnermalt", MALT, 4, LocalDate.now().minusDays(1), LocalDate.now());
        warehouse.addNewProduct(mockProduct.toRecord());

        ProductRecord update = new ProductRecord(mockProduct.getId(), "Simcoe", HOPS, 1, LocalDate.now(), LocalDate.now());
        ProductRecord updatedProduct = warehouse.updateProduct(update);

        assertEquals(1, updatedProduct.rating());
        assertEquals("Simcoe", updatedProduct.name());
        assertEquals(HOPS, updatedProduct.category());
        assertEquals(LocalDate.now(), updatedProduct.updatedAt());
        assertEquals(LocalDate.now().minusDays(1), updatedProduct.createdAt());
    }



}
