package com.portafolio.core.controllers;

import com.portafolio.core.dto.CreateOrderRequest;
import com.portafolio.core.entities.ServiceOrder;
import com.portafolio.core.services.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService orderService;

    @PostMapping
    public ResponseEntity<ServiceOrder> createOrder(@RequestBody CreateOrderRequest request) {
        ServiceOrder newOrder = orderService.createOrder(
            request.device(), 
            request.reportedProblem()
        );
        
        // Retorna un código HTTP 201 (Created) junto con la orden generada
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }
}
