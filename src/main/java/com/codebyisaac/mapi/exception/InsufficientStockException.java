package com.codebyisaac.mapi.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException (String productId, int requested, int available){
        super("Product: " + productId + ": requested" + requested + ", only " + available + " available");
    }
}