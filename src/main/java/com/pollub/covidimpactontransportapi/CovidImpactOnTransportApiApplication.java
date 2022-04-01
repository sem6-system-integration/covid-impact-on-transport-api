package com.pollub.covidimpactontransportapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class CovidImpactOnTransportApiApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(CovidImpactOnTransportApiApplication.class, args);
        openBrowserWithSwaggerUI();
    }

    private static void openBrowserWithSwaggerUI() throws IOException {
        Runtime rt = Runtime.getRuntime();
        var commands = new String[]{"cmd", "/c", "start", "http://localhost:8080/swagger-ui/index.html"};
        rt.exec(commands);
    }

}
