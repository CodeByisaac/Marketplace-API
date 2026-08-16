package com.codebyisaac.mapi.dto;

import lombok.Value;

@Value
public class ErrorDTO {
     String field;
     String errorMessage;
}
