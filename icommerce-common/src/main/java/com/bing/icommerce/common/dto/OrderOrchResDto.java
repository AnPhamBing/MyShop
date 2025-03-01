package com.bing.icommerce.common.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
public class OrderOrchResDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private BigDecimal totalPrice;
    private String status;
}