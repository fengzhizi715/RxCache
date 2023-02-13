package com.safframework.rxcache.converter;

import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.log.LoggerProxy;
import io.protostuff.*;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @FileName: com.safframework.rxcache.converter.ProtostuffUtils
 * @author: Tony Shen
 * @date: 2020-06-21 12:34
 * @version: V1.6
 */
public class ProtostuffUtils {

    /**
     * 需要使用包装类进行序列化/反序列化的class集合
     */
    private static final Set<Class<?>> WRAPPER_SET = new HashSet<>();

    /**
     * 序列化/反序列化包装类 Class 对象
     */
    private static final Class<SerializeDeserializeWrapper> WRAPPER_CLASS = SerializeDeserializeWrapper.class;


    private final static DefaultIdStrategy idStrategy = ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY);

    /**
     * 序列化/反序列化包装类 Schema 对象
     */
    private static final Schema<SerializeDeserializeWrapper> WRAPPER_SCHEMA = RuntimeSchema.createFrom(WRAPPER_CLASS, idStrategy);

    /**
     * 缓存对象及对象schema信息集合
     */
    private static final Map<Class<?>, Schema<?>> CACHE_SCHEMA = new ConcurrentHashMap<>();


    /**
     * 预定义一些Protostuff无法直接序列化/反序列化的对象
     */
    static {
        WRAPPER_SET.add(List.class);
        WRAPPER_SET.add(ArrayList.class);
        WRAPPER_SET.add(CopyOnWriteArrayList.class);
        WRAPPER_SET.add(LinkedList.class);
        WRAPPER_SET.add(Stack.class);
        WRAPPER_SET.add(Vector.class);

        WRAPPER_SET.add(Map.class);
        WRAPPER_SET.add(HashMap.class);
        WRAPPER_SET.add(TreeMap.class);
        WRAPPER_SET.add(Hashtable.class);
        WRAPPER_SET.add(SortedMap.class);

        WRAPPER_SET.add(Set.class);
        WRAPPER_SET.add(SortedSet.class);
        WRAPPER_SET.add(HashSet.class);
        WRAPPER_SET.add(TreeSet.class);

        WRAPPER_SET.add(CacheHolder.class);
        WRAPPER_SET.add(Object.class);

        idStrategy.registerDelegate(new TimestampDelegate());
    }

    /**
     * 注册需要使用包装类进行序列化/反序列化的 Class 对象
     *
     * @param clazz 需要包装的类型 Class 对象
     */
    public static void registerWrapperClass(Class clazz) {
        WRAPPER_SET.add(clazz);
    }

    /**
     * 获取序列化对象类型的schema
     *
     * @param cls 序列化对象的class
     * @param <T> 序列化对象的类型
     * @return 序列化对象类型的schema
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) CACHE_SCHEMA.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls, idStrategy);
            CACHE_SCHEMA.put(cls, schema);
        }
        return schema;
    }

    /**
     * 序列化对象
     *
     * @param obj 需要序列化的对象
     * @param <T> 序列化对象的类型
     * @return 序列化后的二进制数组
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] serialize(T obj) {

        if (obj == null) {
            LoggerProxy.INSTANCE.getLogger().i("序列号对象不能为空","rxcache");
            throw new RxCacheException("序列号对象不能为空");
        }

        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Object serializeObject = obj;
            Schema schema = WRAPPER_SCHEMA;
            if (!WRAPPER_SET.contains(clazz)) {
                schema = getSchema(clazz);
            } else {
                serializeObject = SerializeDeserializeWrapper.builder(obj);
            }
            return ProtostuffIOUtil.toByteArray(serializeObject, schema, buffer);
        } catch (Exception e) {
            LoggerProxy.INSTANCE.getLogger().e("serialize is failed...","rxcache", e);
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化对象
     *
     * @param data  需要反序列化的二进制数组
     * @param clazz 反序列化后的对象class
     * @param <T>   反序列化后的对象类型
     * @return 反序列化后的对象集合
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {

        if (data == null) {
            LoggerProxy.INSTANCE.getLogger().i("反序列号对象不能为空","rxcache");
            throw new RxCacheException("反序列号对象不能为空");
        }

        try {
            if (!WRAPPER_SET.contains(clazz)) {
                T message = clazz.newInstance();
                Schema<T> schema = getSchema(clazz);
                ProtostuffIOUtil.mergeFrom(data, message, schema);
                return message;
            } else {
                SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
                ProtostuffIOUtil.mergeFrom(data, wrapper, WRAPPER_SCHEMA);
                return wrapper.getData();
            }
        } catch (Exception e) {
            LoggerProxy.INSTANCE.getLogger().e("deserialize is failed...","rxcache", e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * protostuff timestamp Delegate
     *
     */
    static class TimestampDelegate implements Delegate<Timestamp> {

        @Override
        public WireFormat.FieldType getFieldType() {
            return WireFormat.FieldType.FIXED64;
        }

        @Override
        public Class<?> typeClass() {
            return Timestamp.class;
        }

        @Override
        public Timestamp readFrom(Input input) throws IOException {
            return new Timestamp(input.readFixed64());
        }

        @Override
        public void writeTo(Output output, int number, Timestamp value, boolean repeated) throws IOException {
            output.writeFixed64(number, value.getTime(), repeated);
        }

        @Override
        public void transfer(Pipe pipe, Input input, Output output, int number, boolean repeated) throws IOException {
            output.writeFixed64(number, input.readFixed64(), repeated);
        }

    }
}