package com.bing.icommerce.common.dto;

import com.bing.icommerce.common.enums.InventoryStatus;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class InventoryResDto {
    private Long orderId;
    private String orderTrackingNumber;
    private Long customerId;
    private String status;
}
