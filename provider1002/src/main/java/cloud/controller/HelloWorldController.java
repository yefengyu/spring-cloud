package cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloWorldController
{
    @GetMapping("/hello")
    @ResponseBody
    public String hello()
    {
        return "hello spring cloud, 1002";
    }
}
