package com.xmj.rpc.sample.server;

import com.xmj.rpc.sample.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello!" + name;
    }
}
