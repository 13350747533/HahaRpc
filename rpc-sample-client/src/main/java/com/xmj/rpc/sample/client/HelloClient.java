package com.xmj.rpc.sample.client;

import com.xmj.rpc.client.RpcProxy;
import com.xmj.rpc.sample.api.HelloService;
import com.xmj.rpc.sample.api.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient {

    public static void main(String[] args) throws  Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);

        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("world");
        System.out.println(result);

        HelloService helloService2 = rpcProxy.create(HelloService.class, "sample.hello2");
        String result2 = helloService2.hello("世界");
//        String result2 = helloService2.hello(new Person(1, "tom"));
        System.out.println(result2);

        System.exit(0);
    }

}
