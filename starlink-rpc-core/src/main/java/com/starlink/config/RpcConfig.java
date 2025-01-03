package com.starlink.config;

import com.starlink.constants.SerializerKeys;
import com.starlink.model.RegistryConfig;

/**
 * RPC 框架配置
 */
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

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getSerializer() {
        return serializer;
    }

    public void setSerializer(String serializer) {
        this.serializer = serializer;
    }

    @Override
    public String toString() {
        return "RpcConfig{" +
               "name='" + name + '\'' +
               ", version='" + version + '\'' +
               ", serverHost='" + serverHost + '\'' +
               ", serverPort=" + serverPort +
               ", serializer='" + serializer + '\'' +
               '}';
    }
}
