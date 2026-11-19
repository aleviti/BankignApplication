package com.example.service;

import org.springframework.stereotype.Service;

import com.example.dto.OrderStatus;

@Service
public class OrderService {
    public OrderStatus getOrderStatus(String orderId){
        return new OrderStatus(orderId, "SHIPPED",  "TOMMOROW");
    }
}