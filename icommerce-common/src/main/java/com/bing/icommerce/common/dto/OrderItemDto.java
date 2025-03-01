package com.bing.icommerce.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@ToString
public class OrderItemDto {
    private Long id;
    private Long productId;
    private BigDecimal unitPrice;
    private int quantity;
}