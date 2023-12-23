package com.cmn.commune_backend;

import com.cmn.commune_backend.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Import({ SecurityConfig.class})
@SpringBootApplication
public class CommuneBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuneBackendApplication.class, args);
    }

}
