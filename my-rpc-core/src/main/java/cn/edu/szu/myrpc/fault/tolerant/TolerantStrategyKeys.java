package cn.edu.szu.myrpc.fault.tolerant;

/**
 * 容错策略键名常量
 */
public interface TolerantStrategyKeys {

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";

    /**
     * 故障转移
     */
    String FAIL_OVER = "failOver";

}
