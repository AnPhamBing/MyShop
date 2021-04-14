package com.bing.icommerce.common.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString
public class PaymentReqDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private BigDecimal availableBalance;
    private BigDecimal totalPrice;
}
