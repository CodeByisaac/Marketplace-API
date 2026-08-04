package com.codebyisaac.mapi.mapper;

import com.codebyisaac.mapi.dto.OrderItemResponse;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.entity.Order;

import java.util.List;

public class OrderMapper {
    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map( i -> new OrderItemResponse(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPriceAtPurchase()))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                items);
    }
}
