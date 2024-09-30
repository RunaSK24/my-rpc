package cn.edu.szu.myrpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtils {
    /**
     * 加载配置对象
     * @param tClass 配置类类名
     * @param prefix 配置前缀
     * @param environment 环境名称
     * @return 配置对象
     * @param <T> 配置类
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix, String environment) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(tClass, prefix);
    }

    /**
     * 加载配置对象
     * @param tClass 配置类类名
     * @param prefix 配置前缀
     * @return 配置对象
     * @param <T> 配置类
     */
    public static <T> T loadConfig(Class<T> tClass, String prefix) {
        return loadConfig(tClass, prefix, "");
    }
}
