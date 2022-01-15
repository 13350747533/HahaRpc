package com.xmj.rpc.common.util.serializer;

public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
