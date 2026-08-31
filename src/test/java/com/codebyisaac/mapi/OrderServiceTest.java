package com.codebyisaac.mapi;

import com.codebyisaac.mapi.dto.OrderItemRequest;
import com.codebyisaac.mapi.dto.OrderPlacementRequest;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.entity.Product;
import com.codebyisaac.mapi.entity.User;
import com.codebyisaac.mapi.exception.InsufficientStockException;
import com.codebyisaac.mapi.mapper.OrderMapper;
import com.codebyisaac.mapi.repository.OrderRepository;
import com.codebyisaac.mapi.repository.ProductRepository;
import com.codebyisaac.mapi.repository.UserRepository;
import com.codebyisaac.mapi.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //standard JUnit 5 extension for Mockito unit test
public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Create Order - success")
    void createOrder_ShouldSucceed_WhenDataAndStockAreValid() {
        String userId = "user-123";
        String productId = "prod-999";

        OrderPlacementRequest request = new OrderPlacementRequest(userId,
                List.of(new OrderItemRequest(productId, 2)));


        User mockUser = new User();
        mockUser.setId(userId);

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Laptop");
        mockProduct.setPrice(new BigDecimal("1000.00"));
        mockProduct.setStockQuantity(10);


        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        //mocking the save op to return whatever entity is passed to it
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse mockResponse = new OrderResponse("order-123", userId, new BigDecimal("2000.00"),
                java.time.LocalDateTime.now(), List.of());
        when(orderMapper.toResponse(any())).thenReturn(mockResponse);

        OrderResponse response = orderService.createOrder(userId, request);

        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals(8, mockProduct.getStockQuantity());

        verify(orderRepository, times(1)).save(any()); //verify database write was triggered
    }

    @DisplayName("Create order - Throws InsufficientStockException")
    @Test
    void createOrder_ShouldThrowException_WhenStockIsTooLow(){
        String userId = "user-123";
        String productId = "prod-999";

        OrderPlacementRequest request = new OrderPlacementRequest(userId,
                List.of(new OrderItemRequest(productId, 5)));

        User mockUser = new User();
        mockUser.setId(userId);

        Product mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Keyboard");
        mockProduct.setPrice(new BigDecimal("1000.00"));
        mockProduct.setStockQuantity(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(userId, request));

        verify(orderRepository, never()).save(any());
    }
}
