package cn.edu.szu.myrpc.fault.tolerant;

import cn.edu.szu.myrpc.RpcApplication;
import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.fault.retry.RetryStrategy;
import cn.edu.szu.myrpc.fault.retry.RetryStrategyFactory;
import cn.edu.szu.myrpc.loadbalancer.LoadBalancer;
import cn.edu.szu.myrpc.loadbalancer.LoadBalancerFactory;
import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.model.RpcResponse;
import cn.edu.szu.myrpc.model.ServiceMetaInfo;
import cn.edu.szu.myrpc.server.tcp.VertxTcpClient;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障转移（转移到其他服务节点）
 */
@Slf4j
public class FailOverTolerantStrategy implements TolerantStrategy {

    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        RpcResponse rpcResponse = new RpcResponse();

        // 取得 RPC 配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        // 取得服务列表
        List<ServiceMetaInfo> serviceMetaInfoList = (List<ServiceMetaInfo>) context.get("serviceMetaInfoList");
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            log.info("没有其他可用服务可供故障转移");
            return rpcResponse;
        }

        // 取得 RPC 请求
        RpcRequest rpcRequest = (RpcRequest) context.get("rpcRequest");
        if (rpcRequest == null) {
            return rpcResponse;
        }

        // 负载均衡
        LoadBalancer loadBalancer = LoadBalancerFactory.getInstance(rpcConfig.getLoadBalancer());
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("methodName", rpcRequest.getMethodName());
        ServiceMetaInfo selectedServiceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);

        // 再次通过TCP客户端发送RPC请求解析应答
        try {
            // 获得重试策略
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            rpcResponse = retryStrategy.doRetry(() ->
                    VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo)
            );
        } catch (Exception ex) {
            // 获得容错策略
            TolerantStrategy tolerantStrategy = TolerantStrategyFactory.getInstance(rpcConfig.getTolerantStrategy());
            // 设置传入信息
            Map<String, Object> newContext = new HashMap<>();
            serviceMetaInfoList.remove(selectedServiceMetaInfo); // 去除当前这个失效的服务数据
            newContext.put("serviceMetaInfoList", serviceMetaInfoList);
            newContext.put("rpcRequest", rpcRequest);
            rpcResponse = tolerantStrategy.doTolerant(newContext, ex);
        }

        return rpcResponse;
    }

}
