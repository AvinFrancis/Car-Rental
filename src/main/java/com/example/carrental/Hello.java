package com.example.carrental;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Hello {

    @GetMapping("abc")
    public String Hell0() {
        return "Hello";
    }
    

}
