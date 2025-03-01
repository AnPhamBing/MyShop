package com.bing.icommerce.core.config;

//import com.bing.icommerce.common.dto.OrchestratorRequestDTO;
import com.bing.icommerce.common.dto.OrderOrchReqDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Configuration
public class OrderConfig {

    @Bean
    public DirectProcessor<OrderOrchReqDto> publisher(){
        return DirectProcessor.create();
    }

    @Bean
    public FluxSink<OrderOrchReqDto> sink(DirectProcessor<OrderOrchReqDto> publisher){
        return publisher.sink();
    }

}
