package com.xmj.rpc.registry;

/**
 * 服务注册接口
 */
public interface ServiceRegistry {

    /**
     *
     * @param serviceName  服务名称
     * @param serviceAddress  服务地址
     */
    void registry(String serviceName, String serviceAddress);
}
