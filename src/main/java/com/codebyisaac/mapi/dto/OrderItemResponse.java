package com.codebyisaac.mapi.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class OrderItemResponse {
    String productId;
    String productName;
    Integer quantity;
    BigDecimal priceAtPurchase;
}