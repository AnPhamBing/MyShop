package com.bing.icommerce.payment.service;

import com.bing.icommerce.common.constants.StatusConst;
import com.bing.icommerce.common.dto.PaymentReqDto;
import com.bing.icommerce.common.dto.PaymentResDto;
import com.bing.icommerce.common.dto.PaymentResponseDTO;
import com.bing.icommerce.common.enums.PaymentStatus;
import com.bing.icommerce.common.dto.PaymentRequestDTO;
import com.bing.icommerce.payment.dao.CustomerRepository;
import com.bing.icommerce.payment.entity.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    private CustomerRepository customerRepository;

    public PaymentResDto debit(final PaymentReqDto requestDto){
        // <inspect>
        System.out.println("\n---------------\n");
        System.out.println("PaymentReqDto : \n" + requestDto.toString());
        System.out.println("\n---------------\n");

        PaymentResDto responseDTO = new PaymentResDto();
        responseDTO.setOrderId(requestDto.getOrderId());
        responseDTO.setOrderTrackingNumber(requestDto.getOrderTrackingNumber());
        responseDTO.setCustomerId(requestDto.getCustomerId());
        responseDTO.setTotalPrice(requestDto.getTotalPrice());

        responseDTO.setStatus(StatusConst.PAYMENT_REJECTED);
        //Customer customer = customerRepository.getOne(requestDto.getCustomerId());
        Optional<Customer> optionalCustomer = customerRepository.findById(requestDto.getCustomerId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            BigDecimal balance = customer.getAvailableBalance();
            if(balance.compareTo(requestDto.getTotalPrice()) >= 0){
                responseDTO.setStatus(StatusConst.PAYMENT_APPROVED);
                customer.setAvailableBalance(balance.subtract(requestDto.getTotalPrice()));
                customerRepository.save(customer);
            }
        } else {
            // <inspect>
            System.out.println("\n----------\n");
            System.out.println("FAILED Getting Customer : " + requestDto.getCustomerId());
            System.out.println("\n----------\n");
        }

        return responseDTO;
    }

    public void credit(final PaymentReqDto requestDto){
        //Customer customer = customerRepository.getOne(requestDto.getCustomerId());
        Optional<Customer> optionalCustomer = customerRepository.findById(requestDto.getCustomerId());
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setAvailableBalance(customer.getAvailableBalance().add(requestDto.getTotalPrice()));
            customerRepository.save(customer);
        } else {
            // <inspect>
            System.out.println("\n----------\n");
            System.out.println("FAILED Getting Customer : " + requestDto.getCustomerId());
            System.out.println("\n----------\n");
        }
    }

}
