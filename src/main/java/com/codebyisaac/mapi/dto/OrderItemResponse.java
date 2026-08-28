package com.codebyisaac.mapi.dto;

import java.math.BigDecimal;

public record OrderItemResponse(String productId, String productName, Integer quantity, BigDecimal priceAtPurchase) {
}