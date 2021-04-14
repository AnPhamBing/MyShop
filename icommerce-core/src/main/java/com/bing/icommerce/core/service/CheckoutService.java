package com.bing.icommerce.core.service;

import com.bing.icommerce.core.dto.Purchase;
import com.bing.icommerce.core.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(Purchase purchase);
}
