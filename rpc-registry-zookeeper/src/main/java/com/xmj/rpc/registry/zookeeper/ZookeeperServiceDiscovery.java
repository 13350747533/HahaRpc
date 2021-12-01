package com.xmj.rpc.registry.zookeeper;

import com.xmj.rpc.common.util.CollectionUtil;
import com.xmj.rpc.registry.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ZookeeperServiceDiscovery implements ServiceDiscovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class);

    private String zkAddress;

    public ZookeeperServiceDiscovery(String zkAddress){
        this.zkAddress = zkAddress;
    }

    @Override
    public String discovery(String serviceName) {
        //创建zookeeper节点
        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        LOGGER.debug("connect zookeeper");
        try{
            //获取service节点
            String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName;
            if(!zkClient.exists(servicePath)){
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if(CollectionUtil.isEmpty(addressList)){
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            //获取 address 节点

            String address;
            int size = addressList.size();
            if(size == 1) {
                //若只有一个地址
                address = addressList.get(0);
                LOGGER.debug("get only address node: {}", address);
            }else{
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get random address node: {}", address);
            }
            //获取address节点的值
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        }finally {
            zkClient.close();
        }

    }
}
