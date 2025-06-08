package com.starlink.proxy;

import cn.hutool.core.collection.CollUtil;
import com.starlink.RpcApplication;
import com.starlink.config.RpcConfig;
import com.starlink.constants.RpcConstants;
import com.starlink.fault.retry.RetryStrategy;
import com.starlink.fault.retry.RetryStrategyFactory;
import com.starlink.loadbalancer.LoadBalancerFactory;
import com.starlink.model.RpcRequest;
import com.starlink.model.RpcResponse;
import com.starlink.model.ServiceMetaInfo;
import com.starlink.registry.Registry;
import com.starlink.registry.RegistryFactory;
import com.starlink.server.client.VertxClientFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // 将调用方法名（请求路径）作为负载均衡参数
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put("methodName", rpcRequest.getMethodName());

            // 负载均衡策略
            ServiceMetaInfo selectedServiceMetaInfo = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer())
                    .select(requestParams, serviceMetaInfoList);

            // 使用重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(() ->
                    VertxClientFactory.getInstance(rpcConfig.getProtocol())
                            .doRequest(rpcRequest, selectedServiceMetaInfo)
            );

            return rpcResponse.getData();
        } catch (Exception e) {
            log.error("请求发送失败.");
        }
        return null;
    }
}
