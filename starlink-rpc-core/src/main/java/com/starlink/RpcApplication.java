package com.starlink;

import com.starlink.config.RpcConfig;
import com.starlink.constants.RpcConstants;
import com.starlink.config.RegistryConfig;
import com.starlink.registry.Registry;
import com.starlink.registry.RegistryFactory;
import com.starlink.utils.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RPC 框架应用
 * 相当于 holder，存放了项目全局用到的变量。双检锁单例模式实现
 */
public class RpcApplication {

    private static final Logger logger = LoggerFactory.getLogger(RpcApplication.class);

    private static volatile RpcConfig rpcConfig;

    /**
     * 保证方法仅需初始化一次
     */
    private static final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        logger.info("rpc init, config = {}", newRpcConfig.toString());

        // 循环直到初始化完成
        while (!initialized.get()) {
            // 只有一个线程能进入下面的代码块
            if (initialized.compareAndSet(false, true)) {
                rpcConfig = newRpcConfig;
                // 注册中心初始化
                RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
                Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
                registry.init(registryConfig);
                logger.info("registry init, config = {}", registryConfig);

                // 创建并注册 ShuntDown Hook，JVM退出时执行操作
                Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
            }
        }

    }


    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstants.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败，使用默认值
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     *
     * @return
     */
    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
