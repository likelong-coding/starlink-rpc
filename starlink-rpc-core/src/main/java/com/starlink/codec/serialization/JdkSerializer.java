package com.starlink.codec.serialization;

import java.io.*;

/**
 * JDK 序列化器
 */
public class JdkSerializer implements Serializer {

    /**
     * 序列化
     *
     * @param object 序列化对象
     * @param <T>    泛型
     * @return 字节数组
     */
    @Override
    public <T> byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * 反序列化
     *
     * @param bytes 字节数组
     * @param type  class对象
     * @param <T>   泛型
     * @return 对象
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            return type.cast(objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
