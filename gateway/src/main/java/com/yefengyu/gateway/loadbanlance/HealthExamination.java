package com.yefengyu.gateway.loadbanlance;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class HealthExamination implements IPing
{
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public boolean isAlive(Server server)
    {
        String url = "http://" + server.getId() + "/heath";
        try
        {
            ResponseEntity<String> heath = restTemplate.getForEntity(url, String.class);
            if (heath.getStatusCode() == HttpStatus.OK)
            {
                System.out.println("ping " + url + " success and response is " + heath.getBody());
                return true;
            }
            System.out.println("ping " + url + " error and response is " + heath.getBody());
            return false;
        }
        catch (Exception e)
        {
            System.out.println("ping " + url + " failed");
            return false;
        }
    }
}
