package com.xmj.rpc.server;

import com.xmj.rpc.common.bean.RpcRequest;
import com.xmj.rpc.common.bean.RpcResponse;
import com.xmj.rpc.common.codec.RpcDecoder;
import com.xmj.rpc.common.codec.RpcEncoder;
import com.xmj.rpc.common.util.StringUtil;
import com.xmj.rpc.common.util.ThreadUtil;
import com.xmj.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private String serviceAddress;

    Map<String, Object> handlerMap = new HashMap<>();

    private ServiceRegistry serviceRegistry;

    public NettyServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        this.serviceAddress = serviceAddress;
        this.serviceRegistry = serviceRegistry;
    }

    public NettyServer(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }


    private Thread thread;


    public void start() {
        thread = new Thread(new Runnable() {

//            ExecutorService pool = Executors.newCachedThreadPool();
            ThreadPoolExecutor threadPoolExecutor = ThreadUtil.makeServerThreadPool(NettyServer.class.getSimpleName(),
        16,32);
            @SneakyThrows
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try{
                    //创建并初始化Netty服务器 BootStrap 对象
                    ServerBootstrap bootstrap = new ServerBootstrap();
                    bootstrap.group(bossGroup, workerGroup);
                    bootstrap.channel(NioServerSocketChannel.class);
                    bootstrap.childHandler((new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //解码，编码，并处理请求
                            pipeline.addLast(new RpcDecoder(RpcRequest.class));
                            pipeline.addLast(new RpcEncoder(RpcResponse.class));
                            pipeline.addLast(new RpcServerHandler(handlerMap, threadPoolExecutor));
                        }
                    }));
                    bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
                    bootstrap.childOption(ChannelOption.SO_BACKLOG, 1024);
                    //获取RPC服务器的IP和端口号
                    String[] addressArray = StringUtil.split(serviceAddress,":");
                    String ip = addressArray[0];
                    int port = Integer.parseInt(addressArray[1]);
                    //启动RPC服务器
                    ChannelFuture future = bootstrap.bind(ip, port).sync();
                    //注册RPC服务器
//                    LOGGER.debug("serviceRegistry is {}", serviceRegistry);
                    if (serviceRegistry != null) {
                        LOGGER.debug("1 handleMap is {}", handlerMap);
                        for(String interfaceName : handlerMap.keySet()){
                            serviceRegistry.registry(interfaceName, serviceAddress);
                            LOGGER.debug("registry service: {} => {}", interfaceName, serviceAddress);
                        }
                    }
                    LOGGER.debug("server started on port {}", port);
                    //关闭 RPC服务器
                    future.channel().closeFuture().sync();
                }finally{
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                }
            }
        });

        thread.start();
    }

}
