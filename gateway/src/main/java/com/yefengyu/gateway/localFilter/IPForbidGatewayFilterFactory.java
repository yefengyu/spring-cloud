package com.yefengyu.gateway.localFilter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@Order(99)
public class IPForbidGatewayFilterFactory
    extends AbstractGatewayFilterFactory<IPForbidGatewayFilterFactory.Config>
{

    public IPForbidGatewayFilterFactory()
    {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder()
    {
        return Arrays.asList("forbidIp");
    }

    @Override
    public GatewayFilter apply(Config config)
    {
        return (exchange, chain) -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            if (config.getForbidIp().equals(ip))
            {
                return chain.filter(exchange);
            }
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();

        };
    }

    static public class Config
    {
        private String forbidIp;

        public String getForbidIp()
        {
            return forbidIp;
        }

        public void setForbidIp(String forbidIp)
        {
            this.forbidIp = forbidIp;
        }
    }
}
