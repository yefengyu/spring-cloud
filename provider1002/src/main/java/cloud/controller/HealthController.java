package cloud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController
{
    @GetMapping("/heath")
    public String heath()
    {
        return "ok";
    }
}
