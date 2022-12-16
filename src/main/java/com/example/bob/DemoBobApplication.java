package com.example.bob;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoBobApplication {

    private static final Log logger = LogFactory.getLog(DemoBobApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoBobApplication.class, args);
    }

}
