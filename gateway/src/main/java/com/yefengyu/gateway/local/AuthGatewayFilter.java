package com.yefengyu.gateway.local;

import com.yefengyu.gateway.utitls.AuthUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class AuthGatewayFilter implements GatewayFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //获取header的参数
        String name = exchange.getRequest().getHeaders().getFirst("name");
        String password = exchange.getRequest().getHeaders().getFirst("password");
        boolean permitted = AuthUtil.isPermitted(name, password);//权限比较
        if (permitted)
        {
            return chain.filter(exchange);
        }
        else
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder()
    {
        return 10;
    }
}
