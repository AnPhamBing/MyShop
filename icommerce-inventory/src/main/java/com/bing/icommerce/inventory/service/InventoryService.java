package com.bing.icommerce.inventory.service;

import com.bing.icommerce.common.constants.StatusConst;
import com.bing.icommerce.common.dto.*;
import com.bing.icommerce.common.enums.InventoryStatus;
import com.bing.icommerce.inventory.dao.ProductRepository;
import com.bing.icommerce.inventory.entity.Product;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class InventoryService {

    private ProductRepository productRepository;

    public InventoryResDto deductInventory(final InventoryReqDto requestDto){
        // <inspect>
        System.out.println("\n---------------\n");
        System.out.println("InventoryReqDto : \n" + requestDto.toString());
        System.out.println("\n---------------\n");

        boolean allAvailable = true;
        for (OrderItemDto item : requestDto.getOrderItems()) {
            // <inspect>
            System.out.println("\n----------\n");
            System.out.println("OrderItemDto : " + item.toString());
            System.out.println("\n----------\n");
            //Product product = productRepository.getOne(item.getProductId());
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                if (product.getUnitsInStock() < item.getQuantity()) {
                    allAvailable = false;
                    break;
                }
            } else {
                // <inspect>
                System.out.println("\n----------\n");
                System.out.println("FAILED Getting Product : " + item.getProductId());
                System.out.println("\n----------\n");

                allAvailable = false;
                break;
            }
        }
        InventoryResDto responseDto = new InventoryResDto();
        responseDto.setOrderId(requestDto.getOrderId());
        responseDto.setOrderTrackingNumber(requestDto.getOrderTrackingNumber());
        responseDto.setCustomerId(requestDto.getCustomerId());

        responseDto.setStatus(StatusConst.UNAVAILABLE);
        if(allAvailable){
            responseDto.setStatus(StatusConst.AVAILABLE);
            List<Product> productList = new ArrayList<>();
            for (OrderItemDto item : requestDto.getOrderItems()) {
                //Product product = productRepository.getOne(item.getProductId());
                Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    product.setUnitsInStock(product.getUnitsInStock() - item.getQuantity());
                    productList.add(product);
                } else {
                    // <inspect>
                    System.out.println("\n----------\n");
                    System.out.println("FAILED Getting Product : " + item.getProductId());
                    System.out.println("\n----------\n");
                }
            }
            productRepository.saveAll(productList);
        }

        return responseDto;
    }

    public void addInventory(final InventoryReqDto requestDto){
        List<Product> productList = new ArrayList<>();
        for (OrderItemDto item : requestDto.getOrderItems()) {
            //Product product = productRepository.getOne(item.getProductId());
            Optional<Product> optionalProduct = productRepository.findById(item.getProductId());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setUnitsInStock(product.getUnitsInStock() + item.getQuantity());
                productList.add(product);
            } else {
                // <inspect>
                System.out.println("\n----------\n");
                System.out.println("FAILED Getting Product : " + item.getProductId());
                System.out.println("\n----------\n");
            }

        }
        productRepository.saveAll(productList);
    }
}
