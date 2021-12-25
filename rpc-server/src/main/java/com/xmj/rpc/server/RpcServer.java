package com.xmj.rpc.server;

import com.xmj.rpc.common.bean.RpcRequest;
import com.xmj.rpc.common.bean.RpcResponse;
import com.xmj.rpc.common.codec.RpcDecoder;
import com.xmj.rpc.common.codec.RpcEncoder;
import com.xmj.rpc.common.util.StringUtil;
import com.xmj.rpc.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * 发布RPC服务
 */

public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

//    private String serviceAddress;
//
//    private ServiceRegistry serviceRegistry;


    /**
     * 存放服务名和服务对象之间的映射关系
     */
//    private Map<String, Object> handlerMap =new HashMap<>();

    public RpcServer(String serviceAddress) {
        super(serviceAddress);
//        this.serviceAddress = serviceAddress;
    }

    public RpcServer(String serviceAddress, ServiceRegistry serviceRegistry) {
        super(serviceAddress, serviceRegistry);
//        this.serviceAddress = serviceAddress;
//        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //扫描带有RpcService注解的类并初始化 handleMapduixiang
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(MapUtils.isNotEmpty(serviceBeanMap)){
            for(Object serviceBean : serviceBeanMap.values()) {
                RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = rpcService.value().getName();
                String serviceVersion = rpcService.version();
                if (StringUtil.isNotEmpty(serviceVersion)){
                    serviceName += "-" + serviceVersion;
                }
                handlerMap.put(serviceName, serviceBean);
//                LOGGER.debug("handlerMap is {}", handlerMap);
            }
        }
    }
}
