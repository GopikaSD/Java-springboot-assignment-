
package com.example.employeemanagement;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
class SingletonDemo {

    @PostConstruct
    public void init() {
        System.out.println("Singleton bean initialized");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Singleton bean destroyed");
    }
}

@Component
@Scope("prototype")
class PrototypeDemo {

    @PostConstruct
    public void init() {
        System.out.println("Prototype bean initialized");
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Prototype bean destroyed");
    }
}

@Configuration
class ScopeCheck {

    @Bean
    CommandLineRunner checkScopes(ApplicationContext context) {
        return args -> {
            System.out.println("Singleton same instance: "
                    + (context.getBean(SingletonDemo.class)
                    == context.getBean(SingletonDemo.class)));

            System.out.println("Prototype same instance: "
                    + (context.getBean(PrototypeDemo.class)
                    == context.getBean(PrototypeDemo.class)));
        };
    }
}