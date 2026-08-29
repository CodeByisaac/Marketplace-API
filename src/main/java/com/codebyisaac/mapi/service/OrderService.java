package com.codebyisaac.mapi.service;

import com.codebyisaac.mapi.dto.OrderItemRequest;
import com.codebyisaac.mapi.dto.OrderPlacementRequest;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.entity.Order;
import com.codebyisaac.mapi.entity.OrderItem;
import com.codebyisaac.mapi.entity.Product;
import com.codebyisaac.mapi.entity.User;
import com.codebyisaac.mapi.exception.InsufficientStockException;
import com.codebyisaac.mapi.exception.ProductNotFoundException;
import com.codebyisaac.mapi.exception.UserNotFoundException;
import com.codebyisaac.mapi.mapper.OrderMapper;
import com.codebyisaac.mapi.repository.OrderRepository;
import com.codebyisaac.mapi.repository.ProductRepository;
import com.codebyisaac.mapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Transactional
    public OrderResponse createOrder(String userId, OrderPlacementRequest request) {
        log.info("Creating order: user={} itemCount{}", userId, request.items().size());

        User user = userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException(userId));

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(()-> new ProductNotFoundException(itemReq.productId()));

            if (product.getStockQuantity() < itemReq.quantity()) {
                throw new InsufficientStockException(product.getId(), itemReq.quantity(), product.getStockQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemReq.quantity());
            item.setPriceAtPurchase(product.getPrice());

            order.addOrderItem(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
        }
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}