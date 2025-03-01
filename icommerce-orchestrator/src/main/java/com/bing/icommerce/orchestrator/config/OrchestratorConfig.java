package com.bing.icommerce.orchestrator.config;

//import com.bing.icommerce.common.dto.OrchestratorRequestDTO;
//import com.bing.icommerce.common.dto.OrchestratorResponseDTO;
import com.bing.icommerce.common.dto.OrderOrchReqDto;
import com.bing.icommerce.common.dto.OrderOrchResDto;
import com.bing.icommerce.orchestrator.service.OrchestratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
public class OrchestratorConfig {

    @Autowired
    private OrchestratorService orchestratorService;

    @Bean
    public Function<Flux<OrderOrchReqDto>, Flux<OrderOrchResDto>> processor(){
        return flux -> flux.flatMap(dto -> this.orchestratorService.orderProduct(dto))
                            .doOnNext(dto -> System.out.println("Status : " + dto.getStatus()));
    }

}
