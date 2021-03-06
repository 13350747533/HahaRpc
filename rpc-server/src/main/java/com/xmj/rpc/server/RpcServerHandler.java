package com.xmj.rpc.server;

import com.xmj.rpc.common.bean.RpcRequest;
import com.xmj.rpc.common.bean.RpcResponse;
import com.xmj.rpc.common.util.StringUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Roc服务端处理器
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServerHandler.class);
    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor threadPoolExecutor;

    public RpcServerHandler(Map<String, Object> handlerMap, ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.handlerMap = handlerMap;
    }

    public void channelRead0(final ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        threadPoolExecutor.execute(new Runnable() {
            public void run() {
                //创建并初始化RPC响应对象
                RpcResponse response = new RpcResponse();
                LOGGER.debug("cannelRead0start, request is {}", request);
                response.setRequestId(request.getRequestId());
                try{
                    Object result = handle(request);
                    response.setResult(result);
                } catch (Exception e) {
                    LOGGER.error("handler result failuer", e);
                    response.setException(e);
                }
                LOGGER.debug("channelread0 voer, response is {}", response);
                //写入RPC响应对象并自动关闭连接
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            }
        });
    }

    private Object handle(RpcRequest request) throws Exception {
        //获取服务对象
//        LOGGER.debug("Request is {}", request);
        String serviceName = request.getInterfaceName();
        String serviceVersion = request.getServiceVersion();
        if(StringUtil.isNotEmpty(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }
        Object serviceBean = handlerMap.get(serviceName);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("can not find service bean by key : %s", serviceName));
        }
        // 获取反射调用所需的参数
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParametersTypes();
        Object[] parameters = request.getParameters();
        //反射调用方法
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
        //使用cglib
//        FastClass serviceFastClass = FastClass.create(serviceClass);
//        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//        return serviceFastMethod.invoke(serviceBean, parameters);

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }

}
