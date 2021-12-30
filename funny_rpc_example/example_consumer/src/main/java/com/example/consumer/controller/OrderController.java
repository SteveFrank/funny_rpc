package com.example.consumer.controller;

import com.example.api.OrderService;
import com.funny.rpc.core.annotation.FRpcRemote;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
public class OrderController {


    @FRpcRemote
    private OrderService orderService;

    /**
     * 获取订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    @GetMapping("/getOrder")
    public String getOrder(String userId, String orderNo) {

        return orderService.getOrder(userId, orderNo);
    }

}
