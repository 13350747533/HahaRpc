package com.xmj.rpc.sample.server;

import com.xmj.rpc.sample.api.HelloService;
import com.xmj.rpc.sample.api.Person;
import com.xmj.rpc.server.RpcService;


@RpcService(value = HelloService.class, version = "sample.hello2")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello!" + name;
    }

    @Override
    public String hello(Person person) {
        return "hello" + person.getName();
    }
}
