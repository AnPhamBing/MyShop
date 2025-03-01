package com.bing.icommerce.core.dto;

import com.bing.icommerce.core.entity.Address;
import com.bing.icommerce.core.entity.Customer;
import com.bing.icommerce.core.entity.Order;
import com.bing.icommerce.core.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;

}
