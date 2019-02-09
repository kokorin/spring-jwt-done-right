package com.github.kokorin.springjwtdoneright;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(scanBasePackageClasses = SpringJwtDoneRightApplication.class)
@EnableWebMvc
public class SpringJwtDoneRightApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringJwtDoneRightApplication.class, args);
    }

}

