package com.jace.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GamesApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GamesApplication.class, args);
    }

}
