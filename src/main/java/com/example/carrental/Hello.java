package com.example.carrental;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class Hello {

    @GetMapping("abc")
    public String Hell0() {
        return "Hello";
    }
    

}
