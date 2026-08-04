package com.codebyisaac.mapi.dto;

import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class OrderResponse {
    String id;
    String userId;
    BigDecimal totalPrice;
    LocalDateTime createdAt;
    List<OrderItemResponse> items;
}