package cn.edu.szu.myrpc;

import cn.edu.szu.myrpc.model.RpcRequest;
import cn.edu.szu.myrpc.serializer.Serializer;
import cn.edu.szu.myrpc.serializer.SerializerFactory;
import cn.edu.szu.myrpc.serializer.SerializerKeys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;

public class SerializerTest {
    public static void main(String[] args) throws IOException {
        testSerializer(SerializerFactory.getInstance(SerializerKeys.JDK));
        testSerializer(SerializerFactory.getInstance(SerializerKeys.JSON));
        testSerializer(SerializerFactory.getInstance(SerializerKeys.HESSIAN));
        testSerializer(SerializerFactory.getInstance(SerializerKeys.KRYO));
    }

    private static void testSerializer(Serializer serializer) throws IOException {
        Object[] rpcArgs = new Object[1];
        rpcArgs[0] = new User("zgr");
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName("UserService")
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(rpcArgs)
                .build();

        byte[] serialized = serializer.serialize(rpcRequest);
        RpcRequest rpcRequest1 = serializer.deserialize(serialized, RpcRequest.class);
        System.out.println(rpcRequest1);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User implements Serializable {
        String name;
    }
}
