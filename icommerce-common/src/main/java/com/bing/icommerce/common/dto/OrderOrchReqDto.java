package com.bing.icommerce.common.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
public class OrderOrchReqDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private BigDecimal totalPrice;
    private Set<OrderItemDto> orderItems = new HashSet<>();
    private String status;
}