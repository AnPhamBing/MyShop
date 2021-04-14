package com.bing.icommerce.core.service;

import com.bing.icommerce.common.dto.OrderOrchResDto;
import com.bing.icommerce.core.dao.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderEventUpdateService {

    @Autowired
    private OrderRepository repository;

    @Transactional
    public void updateOrder(final OrderOrchResDto responseDto){
        // <inspect>
        System.out.println("\n---------------\n");
        System.out.println("OrderOrchResDto : \n" + responseDto.toString());
        System.out.println("\n---------------\n");

        this.repository
                .findById(responseDto.getOrderId())
                .ifPresent(order -> {
                    order.setStatus(responseDto.getStatus());
                    this.repository.save(order);
                });
    }
}
