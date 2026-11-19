package com.example.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.example.dto.OrderStatus;
import com.example.service.OrderService;

@Component 
public class OrderTools {
    private final OrderService orderService;

    public OrderTools (OrderService orderService) {
        this.orderService=orderService;
    } 

    @Tool(description="Get the current status of an order")
    public OrderStatus getOrderStatus(String orderId){
        return orderService.getOrderStatus(orderId);
    }
}
