package com.starlink.config;

import com.starlink.constants.SerializerKeys;
import com.starlink.model.RegistryConfig;
import lombok.Data;

/**
 * RPC 框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "starlink-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 协议类型
     */
    private String protocol = "tcp";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";
    
    /**
     * 服务器端口号
     */
    private Integer serverPort = 8000;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;
}
