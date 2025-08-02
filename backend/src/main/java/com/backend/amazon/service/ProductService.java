package com.backend.amazon.service;

import com.backend.amazon.dto.ProductRequest;
import com.backend.amazon.dto.ProductResponse;
import com.backend.amazon.model.Product;
import com.backend.amazon.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> getProductById(String id) {
        return productRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByType(String type) {
        return productRepository.findByType(type).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByRating(Double minRating) {
        return productRepository.findByRatingGreaterThanEqual(minRating).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return productRepository.findByPriceRange(minPrice, maxPrice).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = convertToEntity(request);
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }

    public Optional<ProductResponse> updateProduct(String id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, request);
                    Product savedProduct = productRepository.save(existingProduct);
                    return convertToResponse(savedProduct);
                });
    }

    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private Product convertToEntity(ProductRequest request) {
        Product product = new Product();
        product.setId(request.getId());
        product.setImage(request.getImage());
        product.setName(request.getName());
        product.setPriceCents(request.getPriceCents());
        product.setKeywords(request.getKeywords());
        product.setType(request.getType());
        product.setSizeChartLink(request.getSizeChartLink());

        Product.Rating rating = new Product.Rating();
        rating.setStars(request.getRatingStars());
        rating.setCount(request.getRatingCount());
        product.setRating(rating);

        return product;
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setImage(product.getImage());
        response.setName(product.getName());
        response.setPriceCents(product.getPriceCents());
        response.setKeywords(product.getKeywords());
        response.setType(product.getType());
        response.setSizeChartLink(product.getSizeChartLink());

        ProductResponse.RatingResponse ratingResponse = new ProductResponse.RatingResponse();
        ratingResponse.setStars(product.getRating().getStars());
        ratingResponse.setCount(product.getRating().getCount());
        response.setRating(ratingResponse);

        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest request) {
        if (request.getImage() != null)
            product.setImage(request.getImage());
        if (request.getName() != null)
            product.setName(request.getName());
        if (request.getPriceCents() != null)
            product.setPriceCents(request.getPriceCents());
        if (request.getKeywords() != null)
            product.setKeywords(request.getKeywords());
        if (request.getType() != null)
            product.setType(request.getType());
        if (request.getSizeChartLink() != null)
            product.setSizeChartLink(request.getSizeChartLink());

        if (request.getRatingStars() != null || request.getRatingCount() != null) {
            Product.Rating rating = product.getRating();
            if (rating == null)
                rating = new Product.Rating();
            if (request.getRatingStars() != null)
                rating.setStars(request.getRatingStars());
            if (request.getRatingCount() != null)
                rating.setCount(request.getRatingCount());
            product.setRating(rating);
        }
    }
}