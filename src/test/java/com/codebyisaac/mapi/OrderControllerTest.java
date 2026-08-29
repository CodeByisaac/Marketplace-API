package com.codebyisaac.mapi;

import com.codebyisaac.mapi.controller.OrderController;
import com.codebyisaac.mapi.dto.OrderItemRequest;
import com.codebyisaac.mapi.dto.OrderItemResponse;
import com.codebyisaac.mapi.dto.OrderPlacementRequest;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.exception.InsufficientStockException;
import com.codebyisaac.mapi.exception.ProductNotFoundException;
import com.codebyisaac.mapi.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.security.test.context.support.WithMockUser;



@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    @WithMockUser
    void placeOrder_returns201_withCreatedOrder() throws Exception{
        OrderItemRequest itemReq = new OrderItemRequest("product-123", 2);
        OrderPlacementRequest request = new OrderPlacementRequest("user-1", List.of(itemReq));

        OrderResponse mockResponse = new OrderResponse(
                "order-1", "user-1", new BigDecimal("39.98"),
                LocalDateTime.now(),
                List.of(new OrderItemResponse("product-123" , "Widget", 2, new BigDecimal("19.99")))
        );

        when(orderService.createOrder(eq("user-1"), any(OrderPlacementRequest.class)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.results.id").value("order-1"))
                .andExpect(jsonPath("$.results.items[0].quantity").value(2));
    }

    @Test
    @WithMockUser
    void placeOrder_returns404_whenProductMissing() throws Exception {
        when(orderService.createOrder(any(), any()))
                .thenThrow(new ProductNotFoundException("ghost-id"));

        OrderPlacementRequest request = new OrderPlacementRequest("user-1",
                List.of(new OrderItemRequest("ghost-id", 1)));

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void placeOrder_return409_whenStockInsufficient() throws Exception {
        when(orderService.createOrder(any(), any())).thenThrow(new InsufficientStockException("product-123", 5, 2));

        OrderPlacementRequest request = new OrderPlacementRequest("user-1",
                List.of(new OrderItemRequest("product-123", 5)));

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void placeOrder_return400_whenQuantityInvalid() throws Exception {
        OrderPlacementRequest request = new OrderPlacementRequest("user-1",
                List.of(new OrderItemRequest("product-123", 0)));

        mockMvc.perform(post("/api/orders")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


        verifyNoInteractions(orderService);
    }
}