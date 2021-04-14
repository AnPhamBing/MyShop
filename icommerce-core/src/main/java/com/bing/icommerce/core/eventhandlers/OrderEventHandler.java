package com.bing.icommerce.core.eventhandlers;

import com.bing.icommerce.common.dto.OrderOrchReqDto;
import com.bing.icommerce.common.dto.OrderOrchResDto;
import com.bing.icommerce.core.service.OrderEventUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderEventHandler {

    @Autowired
    private DirectProcessor<OrderOrchReqDto> source;

    @Autowired
    private OrderEventUpdateService service;

    @Bean
    public Supplier<Flux<OrderOrchReqDto>> supplier(){
        return () -> Flux.from(source);
    };

    @Bean
    public Consumer<Flux<OrderOrchResDto>> consumer(){
        return (flux) -> flux.subscribe(responseDto -> this.service.updateOrder(responseDto));
    };

}
