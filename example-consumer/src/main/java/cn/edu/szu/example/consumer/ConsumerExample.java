package cn.edu.szu.example.consumer;

import cn.edu.szu.myrpc.config.RpcConfig;
import cn.edu.szu.myrpc.constant.RpcConstant;
import cn.edu.szu.myrpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpcConfig);
    }
}
