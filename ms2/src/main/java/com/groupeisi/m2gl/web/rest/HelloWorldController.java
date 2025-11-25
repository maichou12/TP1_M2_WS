package com.groupeisi.m2gl.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloWorldController {

    @GetMapping("/helloWorld")
    public ResponseEntity<String> sayHelloWorld() {
        return ResponseEntity.ok("Je suis Maïmouna Sarr Ravie de vous connaitre M Ly !");
    }
}
