package com.codebyisaac.mapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Value;

import java.util.List;

@Value
public class OrderPlacementRequest {

    @NotBlank
    String userId;

    @NotEmpty
    @Valid
    List<OrderItemRequest> items;
}