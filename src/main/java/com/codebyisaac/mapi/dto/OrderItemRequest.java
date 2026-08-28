package com.codebyisaac.mapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.antlr.v4.runtime.misc.NotNull;

public record OrderItemRequest(@NotBlank String productId,
                               @NotNull @Min(value = 1, message = "quantity must be at least 1") Integer quantity) {
}