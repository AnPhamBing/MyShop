package com.bing.icommerce.orchestrator.service.steps;

import com.bing.icommerce.common.constants.StatusConst;
import com.bing.icommerce.common.dto.PaymentReqDto;
//import com.bing.icommerce.common.dto.PaymentRequestDTO;
import com.bing.icommerce.common.dto.PaymentResDto;
//import com.bing.icommerce.common.dto.PaymentResponseDTO;
//import com.bing.icommerce.common.enums.PaymentStatus;
import com.bing.icommerce.orchestrator.service.WorkflowStep;
import com.bing.icommerce.orchestrator.service.WorkflowStepStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class PaymentStep implements WorkflowStep {

    private final WebClient webClient;
    private final PaymentReqDto requestDto;
    private WorkflowStepStatus stepStatus = WorkflowStepStatus.PENDING;

    public PaymentStep(WebClient webClient, PaymentReqDto requestDto) {
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
                    .uri("/payment/debit")
                    .body(BodyInserters.fromValue(this.requestDto))
                    .retrieve()
                    .bodyToMono(PaymentResDto.class)
                    .map(r -> r.getStatus().equals(StatusConst.PAYMENT_APPROVED))
                    .doOnNext(b -> this.stepStatus = b ? WorkflowStepStatus.COMPLETE : WorkflowStepStatus.FAILED);
    }

    @Override
    public Mono<Boolean> revert() {
        return this.webClient
                .post()
                .uri("/payment/credit")
                .body(BodyInserters.fromValue(this.requestDto))
                .retrieve()
                .bodyToMono(Void.class)
                .map(r -> true)
                .onErrorReturn(false);
    }

}
