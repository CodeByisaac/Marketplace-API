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
        log.info("Creating order: user={} itemCount{}", userId, request.getItems().size());

        User user = userRepository.findById(userId).orElseThrow( () -> new UserNotFoundException(userId));

        Order order = new Order();
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(()-> new ProductNotFoundException(itemReq.getProductId()));

            if (product.getStockQuantity() < itemReq.getQuantity()) {
                throw new InsufficientStockException(product.getId(), itemReq.getQuantity(), product.getStockQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantity());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setOrder(order);
            item.setQuantity(itemReq.getQuantity());
            item.setPriceAtPurchase(product.getPrice());

            order.getItems().add(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())));
        }
        order.setTotalPrice(total);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}