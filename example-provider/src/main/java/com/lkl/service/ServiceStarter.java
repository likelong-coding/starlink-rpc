package com.lkl.service;

import com.starlink.RpcApplication;
import com.starlink.config.RpcConfig;
import com.starlink.model.ServiceMetaInfo;
import com.starlink.registry.LocalRegistry;
import com.starlink.registry.Registry;
import com.starlink.registry.RegistryFactory;
import com.starlink.server.VertxServer;
import com.starlink.server.VertxServerFactory;

/**
 * @author likelong
 * @date 2024/12/21 17:07
 * @description
 */
public class ServiceStarter {
    public static void main(String[] args) {
        RpcApplication.init();

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());


        // 注册服务
        String serviceName = HelloService.class.getName();
        LocalRegistry.register(serviceName, HelloServiceImpl.class);

        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(rpcConfig.getVersion());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 启动服务器
        VertxServer server = VertxServerFactory.getInstance(rpcConfig.getProtocol());
        server.doStart(rpcConfig.getServerPort());
    }
}
