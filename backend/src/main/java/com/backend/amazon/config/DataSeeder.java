package com.backend.amazon.config;

import com.backend.amazon.model.Product;
import com.backend.amazon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no products exist
        if (productRepository.count() == 0) {
            seedProducts();
        }
    }

    private void seedProducts() {
        // Sample products from frontend data
        Product product1 = new Product();
        product1.setId("e43638ce-6aa0-4b85-b27f-e1d07eb678c6");
        product1.setImage("images/products/athletic-cotton-socks-6-pairs.jpg");
        product1.setName("Black and Gray Athletic Cotton Socks - 6 Pairs");
        Product.Rating rating1 = new Product.Rating(4.5, 87);
        product1.setRating(rating1);
        product1.setPriceCents(new BigDecimal("1090"));
        product1.setKeywords(Arrays.asList("socks", "sports", "apparel"));
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setId("15b6fc6f-327a-4ec4-896f-486349e85a3d");
        product2.setImage("images/products/intermediate-composite-basketball.jpg");
        product2.setName("Intermediate Size Basketball");
        Product.Rating rating2 = new Product.Rating(4.0, 127);
        product2.setRating(rating2);
        product2.setPriceCents(new BigDecimal("2095"));
        product2.setKeywords(Arrays.asList("sports", "basketballs"));
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setId("83d4ca15-0f35-48f5-b7a3-1ea210004f2e");
        product3.setImage("images/products/adults-plain-cotton-tshirt-2-pack-teal.jpg");
        product3.setName("Adults Plain Cotton T-Shirt - 2 Pack");
        Product.Rating rating3 = new Product.Rating(4.5, 56);
        product3.setRating(rating3);
        product3.setPriceCents(new BigDecimal("799"));
        product3.setKeywords(Arrays.asList("tshirts", "apparel", "mens"));
        product3.setType("clothing");
        product3.setSizeChartLink("images/clothing-size-chart.png");
        productRepository.save(product3);

        Product product4 = new Product();
        product4.setId("54e0eccd-8f36-462b-b68a-8182611d9add");
        product4.setImage("images/products/black-2-slot-toaster.jpg");
        product4.setName("2 Slot Toaster - Black");
        Product.Rating rating4 = new Product.Rating(5.0, 2197);
        product4.setRating(rating4);
        product4.setPriceCents(new BigDecimal("1899"));
        product4.setKeywords(Arrays.asList("toaster", "kitchen", "appliances"));
        productRepository.save(product4);

        Product product5 = new Product();
        product5.setId("3ebe75dc-64d2-4137-8860-1f5a963e534b");
        product5.setImage("images/products/6-piece-white-dinner-plate-set.jpg");
        product5.setName("6 Piece White Dinner Plate Set");
        Product.Rating rating5 = new Product.Rating(4.0, 37);
        product5.setRating(rating5);
        product5.setPriceCents(new BigDecimal("2067"));
        product5.setKeywords(Arrays.asList("plates", "kitchen", "dining"));
        productRepository.save(product5);

        System.out.println("Sample products seeded successfully!");
    }
}