package com.bing.icommerce.core.service;

import com.bing.icommerce.common.constants.Constants;
import com.bing.icommerce.common.constants.StatusConst;
//import com.bing.icommerce.common.dto.OrchestratorRequestDTO;
import com.bing.icommerce.common.dto.OrderItemDto;
import com.bing.icommerce.common.dto.OrderOrchReqDto;
//import com.bing.icommerce.common.dto.OrderRequestDTO;
//import com.bing.icommerce.common.enums.OrderStatus;
import com.bing.icommerce.core.dao.CustomerRepository;
import com.bing.icommerce.core.entity.Customer;
import com.bing.icommerce.core.dto.Purchase;
import com.bing.icommerce.core.dto.PurchaseResponse;
import com.bing.icommerce.core.entity.Order;
import com.bing.icommerce.core.entity.OrderItem;
import net.logstash.logback.marker.Markers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

//import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    private CustomerRepository customerRepository;

    @Autowired
    private FluxSink<OrderOrchReqDto> sink;

    public CheckoutServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    // @Transactional
    public PurchaseResponse placeOrder(Purchase purchase) {

        // retrieve the order info from dto
        Order order = purchase.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchase.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with billingAddress and shippingAddress
        order.setBillingAddress(purchase.getBillingAddress());
        order.setShippingAddress(purchase.getShippingAddress());

        // set status
        order.setStatus(StatusConst.ORDER_CREATED);

        // populate customer with order
        Customer customer = purchase.getCustomer();

        // check if this is an existing customer
        String theEmail = customer.getEmail();

        Customer customerFromDB = customerRepository.findByEmail(theEmail);

        if (customerFromDB != null) {
            // we found them ... let's assign them accordingly
            customer = customerFromDB;
        } else {
            // sign-in bonus :)
            customer.setAvailableBalance(new BigDecimal(200));
        }

        customer.add(order);

        // save to the database
        customerRepository.saveAndFlush(customer);
        // map order to order request
        OrderOrchReqDto orderReqDto = new OrderOrchReqDto();
        orderReqDto.setOrderId(order.getId());
        orderReqDto.setOrderTrackingNumber(order.getOrderTrackingNumber());
        orderReqDto.setCustomerId(customer.getId());
        orderReqDto.setTotalPrice(order.getTotalPrice());
        orderReqDto.setStatus(order.getStatus());
        Set<OrderItemDto> orderItemSet = new HashSet<>();
        orderItems.forEach(item ->
            orderItemSet.add(new OrderItemDto(item.getId(), item.getProductId(), item.getUnitPrice(), item.getQuantity()))
        );
        orderReqDto.setOrderItems(orderItemSet);
        // <inspect>
        System.out.println("\n---------------\n");
        System.out.println("OrderOrchReqDto : \n" + orderReqDto.toString());
        System.out.println("\n---------------\n");
        // <log>
        Map<String, String> fields = new HashMap<>();
        fields.put(Constants.USER_EMAIL, customer.getEmail());
        fields.put(Constants.ORDER_NUMBER, order.getOrderTrackingNumber());
        fields.put(Constants.ACTION, Constants.Actions.PURCHASE);
        LOGGER.debug(Markers.appendEntries(fields), "Order Orchestrator request DTO '{}'", orderReqDto.toString());

        // sink
        this.sink.next(orderReqDto);

        // return a response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {

        // generate a random UUID number (UUID version-4)
        // For details see: https://en.wikipedia.org/wiki/Universally_unique_identifier
        //
        return UUID.randomUUID().toString();
    }
}









