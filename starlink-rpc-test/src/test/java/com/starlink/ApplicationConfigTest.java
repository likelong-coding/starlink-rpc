package com.starlink;

import com.starlink.config.RpcConfig;
import com.starlink.constants.RpcConstants;
import com.starlink.utils.ConfigUtils;
import org.junit.Test;

/**
 * 读取配置
 *
 * @author likelong
 * @date 2024/12/2 23:39
 * @description
 */
public class ApplicationConfigTest {

    @Test
    public void test() {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstants.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpcConfig);
    }
}
