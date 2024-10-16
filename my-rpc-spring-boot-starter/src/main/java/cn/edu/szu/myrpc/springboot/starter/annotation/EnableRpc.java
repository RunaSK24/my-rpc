package cn.edu.szu.myrpc.springboot.starter.annotation;

import cn.edu.szu.myrpc.springboot.starter.bootstrap.RpcConsumerBootstrap;
import cn.edu.szu.myrpc.springboot.starter.bootstrap.RpcInitBootStrap;
import cn.edu.szu.myrpc.springboot.starter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用 RPC 注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootStrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 是否启动 server
     * @return 是否启动
     */
    boolean needServer() default true;

}
