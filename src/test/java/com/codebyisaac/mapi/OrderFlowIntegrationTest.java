package com.codebyisaac.mapi;

import com.codebyisaac.mapi.dto.APIResponse;
import com.codebyisaac.mapi.dto.OrderItemRequest;
import com.codebyisaac.mapi.dto.OrderPlacementRequest;
import com.codebyisaac.mapi.dto.OrderResponse;
import com.codebyisaac.mapi.entity.Product;
import com.codebyisaac.mapi.entity.User;
import com.codebyisaac.mapi.repository.ProductRepository;
import com.codebyisaac.mapi.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureTestRestTemplate
@Import(TestSecurityConfig.class)
class OrderFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Order Flow: Database persists and stock decrements")
    void executeFullOrderFlow(){
        User user = new User();
        user.setFullName("Test User");
        user.setEmail("testuser@test.com");
        user.setPassword("password");

        userRepository.save(user);
        String userId = user.getId();


        Product product = new Product();
        product.setName("test product");
        product.setCategory("test category");
        product.setPrice(new BigDecimal("1000.0"));
        product.setStockQuantity(10);
        productRepository.save(product);
        String productId = product.getId();

        OrderPlacementRequest request = new OrderPlacementRequest(userId,
                List.of(new OrderItemRequest(productId, 3)));

        HttpEntity<OrderPlacementRequest> httpRequest = new HttpEntity<>(request);

        //ParameterizedTypeReference to cleanly unpack APIResponse wrapper class
        ResponseEntity<APIResponse<OrderResponse>> response = restTemplate.exchange(
                "/api/orders",
                HttpMethod.POST,
                httpRequest,
                new ParameterizedTypeReference<APIResponse<OrderResponse>>() {}
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());

        //verify db change
        Product updatedProduct = productRepository.findById(productId).orElseThrow();
        assertEquals(7, updatedProduct.getStockQuantity());
    }
}