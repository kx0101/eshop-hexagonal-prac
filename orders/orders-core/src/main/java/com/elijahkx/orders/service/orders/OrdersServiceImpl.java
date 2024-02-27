package com.elijahkx.orders.service.orders;

import java.util.List;
import java.util.Optional;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.elijahkx.customers.adapters.outbound.rest.CustomersClient;
import com.elijahkx.customers.rest.dto.Customer;
import com.elijahkx.orders.domain.orders.OrderDomain;
import com.elijahkx.orders.outbound.kafka.OrderEventPort;
import com.elijahkx.orders.outbound.persistence.OrdersPort;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersPort ordersPort;

    private final CustomersClient customersClient;

    private final OrderEventPort orderEventPort;

    public OrdersServiceImpl(OrdersPort ordersPort, CustomersClient customersClient,
            OrderEventPort orderEventPort) {
        this.ordersPort = ordersPort;
        this.customersClient = customersClient;
        this.orderEventPort = orderEventPort;
    }

    @Override
    public List<OrderDomain> findByCriteria() {
        return ordersPort.findByCriteria();
    }

    @Override
    public Optional<OrderDomain> findById(Long id) {
        return ordersPort.findById(id);
    }

    @Override
    public OrderDomain addOrder(OrderDomain order) {
        customersClient.findById(order.getCustomerId()).getBody();

        orderEventPort.produce(order);

        return ordersPort.addOrder(order);
    }

    @Override
    public OrderDomain updateOrder(OrderDomain order) {
        return ordersPort.updateOrder(order);
    }

    @Override
    public void deleteOrder(Long id) {
        ordersPort.deleteOrder(id);
    }
}
