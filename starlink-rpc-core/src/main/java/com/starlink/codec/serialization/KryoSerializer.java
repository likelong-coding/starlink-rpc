package com.starlink.codec.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Kryo序列化器
 */
public class KryoSerializer implements Serializer {

    /**
     * kryo 线程不安全，使用 ThreadLocal 保证每个线程只有一个 Kryo
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 设置动态动态序列化和反序列化类，不提前注册所有类（可能有安全问题）
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /**
     * 序列化对象为byte数组
     *
     * @param obj 需要序列化的对象
     * @return 序列化后的byte数组
     */
    public <T> byte[] serialize(T obj) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Output output = new Output(bao);
        kryo.writeObject(output, obj);
        output.close();
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
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Kryo kryo = kryoThreadLocal.get();
        ByteArrayInputStream ba = new ByteArrayInputStream(bytes);
        Input input = new Input(ba);
        T object = kryo.readObject(input, clazz);
        input.close();
        return object;
    }
}