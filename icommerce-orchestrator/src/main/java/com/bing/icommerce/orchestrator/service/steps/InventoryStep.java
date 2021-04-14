package com.bing.icommerce.orchestrator.service.steps;

import com.bing.icommerce.common.constants.StatusConst;
import com.bing.icommerce.common.dto.InventoryReqDto;
//import com.bing.icommerce.common.dto.InventoryRequestDTO;
import com.bing.icommerce.common.dto.InventoryResDto;
//import com.bing.icommerce.common.dto.InventoryResponseDTO;
//import com.bing.icommerce.common.enums.InventoryStatus;
import com.bing.icommerce.orchestrator.service.WorkflowStep;
import com.bing.icommerce.orchestrator.service.WorkflowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class InventoryStep implements WorkflowStep {

    private final WebClient webClient;
    private final InventoryReqDto requestDto;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public InventoryStep(WebClient webClient, InventoryReqDto requestDto) {
        this.webClient = webClient;
        this.requestDto = requestDto;
    }

    @Override
    public WorkflowStepStatus getStatus() {
        return this.stepStatus;
    }

    @Override
    public Mono<Boolean> process() {
        return this.webClient
                .post()
                .uri("/inventory/deduct")
                .body(BodyInserters.fromValue(this.requestDto))
                .retrieve()
                .bodyToMono(InventoryResDto.class)
                .map(r -> r.getStatus().equals(StatusConst.AVAILABLE))
                .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return this.webClient
                    .post()
                    .uri("/inventory/add")
                    .body(BodyInserters.fromValue(this.requestDto))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .map(r ->true)
                    .onErrorReturn(false);
    }
}
