package com.example.demo.domain.stereotype;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

// Application Service
// Domain Service (Business)

@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Business {

}
