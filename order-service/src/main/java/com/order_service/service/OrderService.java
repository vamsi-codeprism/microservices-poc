package com.order_service.service;


import com.order_service.client.ProductClient;
import com.order_service.dto.OrderEvent;
import com.order_service.dto.ProductDTO;
import com.order_service.entity.Order;
import com.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderProducer orderProducer;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public Order createOrder(Order order) {
        if (order.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        ProductDTO product = productClient.getProductById(order.getProductId());
        double total = product.getPrice() * order.getQuantity();
        order.setTotalPrice(total);

        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setStatus("COMPLETED");
        orderEvent.setMessage("Order has been placed successfully");
        orderEvent.setOrder(order);

        orderProducer.sendMessage(orderEvent);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
