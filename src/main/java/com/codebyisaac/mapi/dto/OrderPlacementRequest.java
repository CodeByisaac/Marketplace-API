package com.codebyisaac.mapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderPlacementRequest(@NotBlank String userId, @NotEmpty @Valid List<OrderItemRequest> items) {

}