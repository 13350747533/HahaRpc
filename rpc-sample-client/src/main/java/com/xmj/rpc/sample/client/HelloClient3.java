package com.xmj.rpc.sample.client;

import com.xmj.rpc.client.RpcProxy;
import com.xmj.rpc.sample.api.HelloService;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloClient3 implements Runnable{

    static RpcProxy rpcProxy;
    static Thread t;
    static volatile int count = 0;
    static long start = System.currentTimeMillis();
    static long time;

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();

//        o.wait();
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        rpcProxy = context.getBean(RpcProxy.class);
        HelloClient3 helloClient3 = new HelloClient3();
        for(int i = 0; i < 50; i ++) {
            helloClient3.start();
        }
//        o.notify();
        Thread.sleep(60000);

            long time = System.currentTimeMillis() - start - 60000;

            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println("-----------------------");
            System.out.println("count: " + count);
            System.out.println("time: " + time + "ms");
            System.out.println("tps: " + (double) count / ((double) time / 1000));

    }



    @SneakyThrows
    public void run() {
        HelloService helloService = rpcProxy.create(HelloService.class);
        for (int i = 0; i < 50; i++) {
            String result = helloService.hello("World");
            System.out.println(result);
            count++;
            Thread.sleep(50);
        }
    }

    public void start () {

        if (t == null) {
            t = new Thread (this);
            t.start ();
        }
    }
}

