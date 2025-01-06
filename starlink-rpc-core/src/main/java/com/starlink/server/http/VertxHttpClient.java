package com.starlink.server.http;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.starlink.RpcApplication;
import com.starlink.codec.serialization.Serializer;
import com.starlink.codec.serialization.SerializerFactory;
import com.starlink.config.RpcConfig;
import com.starlink.model.RpcRequest;
import com.starlink.model.RpcResponse;
import com.starlink.model.ServiceMetaInfo;
import com.starlink.server.client.VertxClient;

/**
 * @author likelong
 * @date 2025/1/6 23:31
 * @description
 */
public class VertxHttpClient implements VertxClient {
    @Override
    public RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws Exception {

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 指定序列化器
        final Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        // 序列化
        byte[] bodyBytes = serializer.serialize(rpcRequest);

        HttpResponse httpResponse = HttpRequest.post(serviceMetaInfo.getServiceAddress())
                .body(bodyBytes)
                .execute();
        byte[] result = httpResponse.bodyBytes();
        // 反序列化
        return serializer.deserialize(result, RpcResponse.class);
    }
}
