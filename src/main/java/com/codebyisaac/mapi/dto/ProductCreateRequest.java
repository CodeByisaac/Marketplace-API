package com.codebyisaac.mapi.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String name;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stockQuantity;

    @Size(max = 100)
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