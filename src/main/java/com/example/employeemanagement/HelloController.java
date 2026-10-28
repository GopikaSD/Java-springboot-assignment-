
package com.example.employeemanagement;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }
    @GetMapping("/hello/{name}")
    public String helloPath(
            @org.springframework.web.bind.annotation.PathVariable String name) {
        return "Hello, " + name + "!";
    }
}
