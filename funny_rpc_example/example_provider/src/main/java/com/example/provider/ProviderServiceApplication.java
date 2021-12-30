package com.example.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author frankq
 * @date 2021/12/30
 */
@SpringBootApplication(scanBasePackages = {
        "com.example",
        "com.funny.rpc.core",
        "com.funny.rpc.server",
})
public class ProviderServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProviderServiceApplication.class, args);
    }

}
