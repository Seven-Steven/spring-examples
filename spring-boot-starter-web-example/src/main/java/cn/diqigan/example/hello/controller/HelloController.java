package cn.diqigan.example.hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("hello")
@RestController
public class HelloController {
    @GetMapping("{name}")
    public String name(@PathVariable(name = "name") String name) {
        return name;
    }
}
