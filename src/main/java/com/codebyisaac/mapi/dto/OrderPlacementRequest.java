package com.codebyisaac.mapi.dto;

import java.util.List;

public class OrderPlacementRequest {
    private String userId;
    private List<OrderItemRequest> items;

    public String getUserId() {return userId;}
    public void setUserID(String userId) {this.userId = userId;}

    public List<OrderItemRequest> getItems() {return items;}
    public void setItems(List<OrderItemRequest> items) {this.items = items;}
}