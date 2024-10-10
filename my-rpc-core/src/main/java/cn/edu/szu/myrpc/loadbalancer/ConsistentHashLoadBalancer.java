package cn.edu.szu.myrpc.loadbalancer;

import cn.edu.szu.myrpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {

    /**
     * 一致性哈希环
     */
    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();

    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if (serviceMetaInfoList.isEmpty()) {
            return null;
        }
        int size = serviceMetaInfoList.size();
        if (size == 1) {
            // 只有一个服务，直接选择
            return serviceMetaInfoList.get(0);
        }

        // 初始化虚拟节点环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash, serviceMetaInfo);
            }
        }

        // 获取调用请求的 hash 值
        int hash = getHash(requestParams);

        // 寻找key大于hash的节点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if (entry == null) {
            // 找不到，循环选择开头第一个节点
            entry = virtualNodes.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * 哈希算法（可自定义，这里直接调用Object的hashCode方法）
     * @param key 进行哈希的对象
     * @return 哈希值
     */
    private int getHash(Object key) {
        return key.hashCode();
    }

}
