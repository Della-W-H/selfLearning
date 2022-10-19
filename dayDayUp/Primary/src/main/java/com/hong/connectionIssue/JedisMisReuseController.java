package com.hong.connectionIssue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


import java.util.concurrent.TimeUnit;

/**
 * @author wanghong
 * @date 2022/10/19
 * @apiNote
 */
@Slf4j
public class JedisMisReuseController{

    private static JedisPool jedisPool=new JedisPool(new GenericObjectPoolConfig<>(),"121.43.161.146",6379,20000,"root");

    public void init() {
        try (Jedis jedis = new Jedis("121.43.161.146", 6379){
            {
                auth("root");
            }
        }) {
            Assert.isTrue("OK".equals(jedis.set("a", "1")), "set a = 1 return OK");
            Assert.isTrue("OK".equals(jedis.set("b", "2")), "set b = 2 return OK");
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            jedisPool.close();
        }));
    }


    /**
     * Jedis 继承了 BinaryJedis，BinaryJedis 中保存了单个 Client 的实例，
     * Client 最终继承了 Connection，Connection 中保存了单个 Socket 的实例，和 Socket 对应的两个读写流。
     * 因此，一个 Jedis 对应一个 Socket 连接。
     *
     * BinaryClient 封装了各种 Redis 命令，其最终会调用基类 Connection 的方法，使用 Protocol 类发送命令。
     * 看一下 Protocol 类的 sendCommand 方法的源码，可以发现其发送命令时是直接操作 RedisOutputStream 写入字节。
     *
     * 我们在多线程环境下复用 Jedis 对象，其实就是在复用 RedisOutputStream。
     * 如果多个线程在执行操作，那么既无法确保整条命令以一个原子操作写入 Socket，也无法确保写入后、读取前没有其他数据写到远端
     * @throws InterruptedException
     */
    @SuppressWarnings("all")
    public void wrong() throws InterruptedException {
        Jedis jedis = new Jedis("121.43.161.146", 6379);
        jedis.auth("root");
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String result = jedis.get("a");
                if (!"1".equals(result)) {
                    log.warn("Expect a to be 1 but found {},i:{}", result,i);
                    continue;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                String result = jedis.get("b");
                if (!"2".equals(result)) {
                    log.warn("Expect b to be 2 but found {},i:{}", result,i);
                    continue;
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(5);
    }

    @SuppressWarnings("all")
    public void right() throws InterruptedException {

        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                for (int i = 0; i < 1000; i++) {
                    String result = jedis.get("a");
                    if (!"1".equals(result)) {
                        log.warn("Expect a to be 1 but found {},i:{}", result,i);
                        continue;
                    }
                }
            }
        }).start();
        new Thread(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                for (int i = 0; i < 1000; i++) {
                    String result = jedis.get("b");
                    if (!"2".equals(result)) {
                        log.warn("Expect b to be 2 but found {},i:{}", result,i);
                        continue;
                    }
                }
            }
        }).start();
        TimeUnit.SECONDS.sleep(5);

    }


    public String timeout(@RequestParam("waittimeout") int waittimeout,
                          @RequestParam("conntimeout") int conntimeout) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1);
        config.setMaxWaitMillis(waittimeout);
        try (JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, conntimeout);
             Jedis jedis = jedisPool.getResource()) {
            return jedis.set("test", "test");
        }
    }

    public static void main(String[] args) throws InterruptedException{
        JedisMisReuseController test=new JedisMisReuseController();
        test.init();
        //test.wrong();
        test.right();
    }
}
