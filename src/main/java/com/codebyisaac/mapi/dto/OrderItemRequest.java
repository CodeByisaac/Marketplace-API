package com.codebyisaac.mapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.antlr.v4.runtime.misc.NotNull;

@Value
public class OrderItemRequest {
    @NotBlank
    String productId;

    @NotNull
    @Min(value = 1, message="quantity must be at least 1")
    Integer quantity;
}