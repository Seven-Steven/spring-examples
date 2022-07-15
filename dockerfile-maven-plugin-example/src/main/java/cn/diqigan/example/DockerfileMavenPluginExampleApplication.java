package cn.diqigan.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DockerfileMavenPluginExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DockerfileMavenPluginExampleApplication.class, args);
    }

    @GetMapping("hello")
    public String hello() {
        return "Hello World";
    }
}
