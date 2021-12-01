package com.xmj.rpc.common.bean;

import lombok.Data;

@Data
public class RpcRequest {
    private String requestId;
    private String interfaceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] parametersTypes;
    private Object[] parameters;
}
