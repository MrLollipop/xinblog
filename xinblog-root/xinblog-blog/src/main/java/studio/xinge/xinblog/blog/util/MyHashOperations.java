package studio.xinge.xinblog.blog.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/4
 * @Description
 */
@Component
public class MyHashOperations implements HashOperations {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HashOperations hashOperations;

    /**
     * 补充redis对hash数据ttl时间设置
     *
     * @param key
     * @param hashKey
     * @param value
     * @param timeout
     * @param unit
     * @Author xinge
     * @Description
     * @Date 2022/7/4
     */
    public void setHash(String key, String hashKey, Object value, final long timeout, final TimeUnit unit) {
        hashOperations.put(key, hashKey, value);
        redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public Long delete(Object key, Object... hashKeys) {
        return hashOperations.delete(key, hashKeys);
    }

    @Override
    public Boolean hasKey(Object key, Object hashKey) {
        return hashOperations.hasKey(key, hashKey);
    }

    @Override
    public Object get(Object key, Object hashKey) {
        return hashOperations.get(key, hashKey);
    }

    @Override
    public List multiGet(Object key, Collection hashKeys) {
        return hashOperations.multiGet(key, hashKeys);
    }

    @Override
    public Long increment(Object key, Object hashKey, long delta) {
        return hashOperations.increment(key, hashKey, delta);
    }

    @Override
    public Double increment(Object key, Object hashKey, double delta) {
        return hashOperations.increment(key, hashKey, delta);
    }

    @Override
    public Set keys(Object key) {
        return hashOperations.keys(key);
    }

    @Override
    public Long lengthOfValue(Object key, Object hashKey) {
        return hashOperations.lengthOfValue(key, hashKey);
    }

    @Override
    public Long size(Object key) {
        return hashOperations.size(key);
    }

    @Override
    public void putAll(Object key, Map m) {
        hashOperations.putAll(key, m);
    }

    @Override
    public void put(Object key, Object hashKey, Object value) {
        hashOperations.put(key, hashKey, value);
    }

    @Override
    public Boolean putIfAbsent(Object key, Object hashKey, Object value) {
        return hashOperations.putIfAbsent(key, hashKey, value);
    }

    @Override
    public List values(Object key) {
        return hashOperations.values(key);
    }

    @Override
    public Map entries(Object key) {
        return hashOperations.entries(key);
    }

    @Override
    public Cursor<Map.Entry> scan(Object key, ScanOptions options) {
        return hashOperations.scan(key, options);
    }

    @Override
    public RedisOperations getOperations() {
        return hashOperations.getOperations();
    }
}
