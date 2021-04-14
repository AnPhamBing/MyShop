package com.bing.icommerce.inventory.controller;

import com.bing.icommerce.common.dto.InventoryReqDto;
import com.bing.icommerce.common.dto.InventoryRequestDTO;
import com.bing.icommerce.common.dto.InventoryResDto;
import com.bing.icommerce.common.dto.InventoryResponseDTO;
import com.bing.icommerce.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("inventory")
public class InventoryController {

    @Autowired
    private InventoryService service;

    @PostMapping("/deduct")
    public InventoryResDto deduct(@RequestBody final InventoryReqDto requestDto){
        return this.service.deductInventory(requestDto);
    }

    @PostMapping("/add")
    public void add(@RequestBody final InventoryReqDto requestDto){
        this.service.addInventory(requestDto);
    }

}
