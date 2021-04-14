package com.bing.icommerce.orchestrator.service;

import com.bing.icommerce.common.constants.StatusConst;
import com.bing.icommerce.common.dto.*;
import com.bing.icommerce.common.enums.OrderStatus;
import com.bing.icommerce.orchestrator.service.steps.InventoryStep;
import com.bing.icommerce.orchestrator.service.steps.PaymentStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrchestratorService {

    @Autowired
    @Qualifier("payment")
    private WebClient paymentClient;

    @Autowired
    @Qualifier("inventory")
    private WebClient inventoryClient;

    public Mono<OrderOrchResDto> orderProduct(final OrderOrchReqDto requestDto){
        // <inspect>
        System.out.println("\n---------------\n");
        System.out.println("OrderOrchReqDto : \n" + requestDto.toString());
        System.out.println("\n---------------\n");

        Workflow orderWorkflow = this.getOrderWorkflow(requestDto);
        return Flux.fromStream(() -> orderWorkflow.getSteps().stream())
                .flatMap(WorkflowStep::process)
                .handle(((aBoolean, synchronousSink) -> {
                    if(aBoolean)
                        synchronousSink.next(true);
                    else
                        synchronousSink.error(new WorkflowException("create order failed!"));
                }))
                .then(Mono.fromCallable(() -> getResponseDto(requestDto, StatusConst.ORDER_COMPLETED)))
                .onErrorResume(ex -> this.revertOrder(orderWorkflow, requestDto));

    }

    private Mono<OrderOrchResDto> revertOrder(final Workflow workflow, final OrderOrchReqDto requestDto){
        return Flux.fromStream(() -> workflow.getSteps().stream())
                .filter(wf -> wf.getStatus().equals(WorkflowStepStatus.COMPLETE))
                .flatMap(WorkflowStep::revert)
                .retry(3)
                .then(Mono.just(this.getResponseDto(requestDto, StatusConst.ORDER_CANCELLED)));
    }

    private Workflow getOrderWorkflow(OrderOrchReqDto requestDto){
        WorkflowStep paymentStep = new PaymentStep(this.paymentClient, this.getPaymentRequestDto(requestDto));
        WorkflowStep inventoryStep = new InventoryStep(this.inventoryClient, this.getInventoryRequestDto(requestDto));
        return new OrderWorkflow(List.of(paymentStep, inventoryStep));
    }

    private OrderOrchResDto getResponseDto(OrderOrchReqDto requestDto, String status){
        OrderOrchResDto responseDto = new OrderOrchResDto();
        responseDto.setOrderId(requestDto.getOrderId());
        responseDto.setTotalPrice(requestDto.getTotalPrice());
        responseDto.setCustomerId(requestDto.getCustomerId());
        responseDto.setStatus(status);
        return responseDto;
    }

    private PaymentReqDto getPaymentRequestDto(OrderOrchReqDto requestDto){
        PaymentReqDto paymentRequestDto = new PaymentReqDto();
        paymentRequestDto.setOrderId(requestDto.getOrderId());
        paymentRequestDto.setOrderTrackingNumber(requestDto.getOrderTrackingNumber());
        paymentRequestDto.setCustomerId(requestDto.getCustomerId());
        paymentRequestDto.setTotalPrice(requestDto.getTotalPrice());
        return paymentRequestDto;
    }

    private InventoryReqDto getInventoryRequestDto(OrderOrchReqDto requestDto){
        InventoryReqDto inventoryRequestDTO = new InventoryReqDto();
        inventoryRequestDTO.setOrderId(requestDto.getOrderId());
        inventoryRequestDTO.setOrderTrackingNumber(requestDto.getOrderTrackingNumber());
        inventoryRequestDTO.setCustomerId(requestDto.getCustomerId());
        inventoryRequestDTO.setOrderItems(requestDto.getOrderItems());
        return inventoryRequestDTO;
    }

}
