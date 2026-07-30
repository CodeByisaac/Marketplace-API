package com.codebyisaac.mapi.dto;

import java.math.BigDecimal;

public class ProductCreateRequest {
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;

    private String getName() { return name; }
    private void setName(String name) { this.name = name; }

    private BigDecimal getPrice(){ return price; }
    private void setPrice(BigDecimal price) { this.price = price; }

    private Integer getStockQuantity() { return stockQuantity; }
    private void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }

    private String getCategory() { return category; }
    private void setCategory(String category) { this.category = category; }
}