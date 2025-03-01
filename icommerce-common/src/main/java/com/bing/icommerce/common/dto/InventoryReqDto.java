package com.bing.icommerce.common.dto;

import lombok.Data;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@ToString
public class InventoryReqDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private Set<OrderItemDto> orderItems = new HashSet<>();
}
