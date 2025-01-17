package com.starlink.loadbalancer;


import com.starlink.model.ServiceMetaInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询负载均衡器
 */
public class RoundRobinLoadBalancer  extends AbstractLoadBalancer  {

    /**
     * 当前轮询的下标(原子类防止并发问题)
     */
    private final AtomicInteger currentIndex = new AtomicInteger(0);

    @Override
    protected ServiceMetaInfo select(List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        // 只有一个服务，无需轮询
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            return serviceMetaInfoList.get(0);
        }
        // 取模算法轮询
        int index = currentIndex.getAndIncrement() % size;
        return serviceMetaInfoList.get(index);
    }
}
