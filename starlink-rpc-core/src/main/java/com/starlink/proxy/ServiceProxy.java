package com.starlink.proxy;

import cn.hutool.core.collection.CollUtil;
import com.starlink.RpcApplication;
import com.starlink.config.RpcConfig;
import com.starlink.constants.RpcConstants;
import com.starlink.model.RpcRequest;
import com.starlink.model.RpcResponse;
import com.starlink.model.ServiceMetaInfo;
import com.starlink.registry.Registry;
import com.starlink.registry.RegistryFactory;
import com.starlink.server.client.VertxClientFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理（JDK 动态代理）
 */
@Slf4j
public class ServiceProxy implements InvocationHandler {

    /**
     * 调用代理
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        // 构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        try {
            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstants.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }

            // 负载均衡策略
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);
            RpcResponse rpcResponse = VertxClientFactory.getInstance(rpcConfig.getProtocol())
                    .doRequest(rpcRequest, selectedServiceMetaInfo);

            return rpcResponse.getData();
        } catch (Exception e) {
            log.error("请求发送失败.");
        }
        return null;
    }
}
