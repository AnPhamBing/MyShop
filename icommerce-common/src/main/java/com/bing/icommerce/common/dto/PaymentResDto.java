package com.bing.icommerce.common.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class PaymentResDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private BigDecimal availableBalance;
    private BigDecimal totalPrice;
    private String status;
}
