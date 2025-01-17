package com.starlink.loadbalancer;


import com.starlink.model.ServiceMetaInfo;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡器
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    private final Random random = new Random();

    @Override
    protected ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList) {
        int size = serviceMetaInfoList.size();
        if (size == 0) {
            return null;
        }
        // 只有 1 个服务，不用随机
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        return serviceMetaInfoList.get(random.nextInt(size));
    }
}
