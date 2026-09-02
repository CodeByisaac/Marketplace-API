package com.codebyisaac.mapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@Jacksonized
@Builder
public class APIResponse<T> {
     String status;
     List<ErrorDTO> errors;
     T results;
}