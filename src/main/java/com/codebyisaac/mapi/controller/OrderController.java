package com.codebyisaac.mapi.controller;

import com.codebyisaac.mapi.dto.APIResponse;
import com.codebyisaac.mapi.dto.OrderPlacementRequest;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    public static final String SUCCESS = "Success";
    private final OrderService orderService;

    public OrderController (OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderPlacementRequest request) {

        OrderResponse orderResponse = orderService.createOrder(request.getUserId(), request);

        APIResponse<OrderResponse> responseDTO = APIResponse
                .<OrderResponse>builder()
                .status(SUCCESS)
                .results(orderResponse)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }
}