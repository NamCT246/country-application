package com.nordea.country.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.util.SocketUtils;

@Configuration
public class AppConfigTest {

    public static int API_PORT = SocketUtils.findAvailableTcpPort();

    public static String API_HOST = "http://localhost";

    @Bean
    public String apiHost() {
        return API_HOST + ":" + API_PORT;
    }
}
