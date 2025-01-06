package com.starlink.server.client;

import com.starlink.model.RpcRequest;
import com.starlink.model.RpcResponse;
import com.starlink.model.ServiceMetaInfo;

/**
 * 客户端发送请求
 */
public interface VertxClient {
    RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception;
}
