package com.yefengyu.gateway;

import com.yefengyu.gateway.utitls.AuthUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;


@SpringBootApplication
public class GatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
        springApplication.addListeners(new ApplicationListenerStarted());//增加监听器
        springApplication.run(args);
    }

    private static class ApplicationListenerStarted
        implements ApplicationListener<ApplicationStartedEvent>
    {
        @Override
        public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent)
        {
            //权限初始化数据
            AuthUtil.init();
        }
    }
}
