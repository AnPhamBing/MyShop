package com.bing.icommerce.payment.controller;

import com.bing.icommerce.common.dto.PaymentReqDto;
import com.bing.icommerce.common.dto.PaymentResDto;
import com.bing.icommerce.common.dto.PaymentResponseDTO;
import com.bing.icommerce.payment.service.PaymentService;
import com.bing.icommerce.common.dto.PaymentRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @PostMapping("/debit")
    public PaymentResDto debit(@RequestBody PaymentReqDto requestDto){
        return this.service.debit(requestDto);
    }

    @PostMapping("/credit")
    public void credit(@RequestBody PaymentReqDto requestDto){
        this.service.credit(requestDto);
    }

}
