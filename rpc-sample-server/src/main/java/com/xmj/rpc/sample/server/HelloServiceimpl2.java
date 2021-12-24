package com.xmj.rpc.sample.server;

import com.xmj.rpc.sample.api.HelloService;
import com.xmj.rpc.sample.api.Person;
import com.xmj.rpc.server.RpcService;

@RpcService(HelloService.class)
public class HelloServiceimpl2 implements HelloService {

    @Override
    public String hello(String name) {
        return "你好，" + name;
    }

    @Override
    public String hello(Person person) {
        return "你好，" + person.getName();
    }
}
