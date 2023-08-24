package com.example.configserverya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerYaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerYaApplication.class, args);
    }

}
