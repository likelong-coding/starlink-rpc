package com.starlink.codec.serialization;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian序列化器
 */
public class HessianSerializer implements Serializer {

    /**
     * 序列化对象为byte数组
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的byte数组
     */
    public <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(bao);
        ho.writeObject(obj);
        ho.flush();
        return bao.toByteArray();
    }

    /**
     * 反序列化byte数组为对象
     *
     * @param bytes 需要反序列化的byte数组
     * @param clazz 反序列化对象的类类型
     * @param <T>   反序列化对象的泛型类型
     * @return 反序列化后的对象
     */
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        ByteArrayInputStream ba = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(ba);
        return clazz.cast(hi.readObject());
    }
}