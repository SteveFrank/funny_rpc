package com.example.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author frankq
 * @date 2021/12/30
 */
@SpringBootApplication(scanBasePackages = {
        "com.example.consumer",
        "com.funny.rpc.core",
        "com.funny.rpc.client",
})
public class ConsumerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }

}
