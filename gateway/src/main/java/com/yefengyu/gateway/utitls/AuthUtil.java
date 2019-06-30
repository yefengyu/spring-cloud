package com.yefengyu.gateway.utitls;

import java.util.HashMap;
import java.util.Map;


public final class AuthUtil
{
    private static Map<String, String> map = new HashMap<>();

    private AuthUtil()
    {
    }

    //程序启动的时候加载权限的信息，比如从文件、数据库中加载
    public static void init()
    {
        map.put("tom", "123456");
    }

    //简单判断
    public static boolean isPermitted(String name, String password)
    {
        return map.containsKey(name) && map.get(name).equals(password);
    }

}
