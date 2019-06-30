package com.yefengyu.gateway.loadbanlance;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.ArrayList;
import java.util.List;


public class MyRule extends AbstractLoadBalancerRule
{
    private  int total;

    private  int index;

    List<Server> upList = new ArrayList<>();

    public MyRule()
    {
    }

    public Server choose(ILoadBalancer lb, Object key)
    {
        if (lb == null)
        {
            return null;
        }
        else
        {
            Server server = null;

            while (server == null)
            {
                if (Thread.interrupted())
                {
                    return null;
                }

                List<Server> allList = lb.getAllServers();
                int serverCount = allList.size();
                if (serverCount == 0)
                {
                    return null;
                }

                if (total == 0)
                {
                    upList = lb.getReachableServers();
                }

                if (total < 3)
                {
                    if (upList.size() != lb.getReachableServers().size())
                    {
                        index = 0;
                    }
                    server = lb.getReachableServers().get(index);
                    total++;
                }
                else
                {
                    total = 0;
                    index++;
                    if (index >= lb.getReachableServers().size())
                    {
                        index = 0;
                    }
                }

                if (server == null)
                {
                    Thread.yield();
                }
                else
                {
                    if (server.isAlive())
                    {
                        return server;
                    }

                    server = null;
                    Thread.yield();
                }
            }

            return server;
        }
    }

    public Server choose(Object key)
    {
        return this.choose(this.getLoadBalancer(), key);
    }

    public void initWithNiwsConfig(IClientConfig clientConfig)
    {
    }
}

