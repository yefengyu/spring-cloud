package com.yefengyu.gateway.local;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object>
{
    @Override
    public GatewayFilter apply(Object config)
    {
        return new AuthGatewayFilter();
    }
}
