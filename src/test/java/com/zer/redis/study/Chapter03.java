package com.zer.redis.study;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zer
 * @create 2016-11-24 16:21
 */
public class Chapter03 {
    private static Jedis conn = new Jedis("192.168.11.81");

    @Test
    public void stringTest() { //当没有键key时运行
        System.out.println(conn.get("key")); //null
        System.out.println(conn.incr("key")); //1
        System.out.println(conn.incrBy("key", 15)); //16
        System.out.println(conn.decr("key")); //15
        System.out.println(conn.get("key")); //15
        System.out.println(conn.set("key", "13")); //OK
        System.out.println(conn.incr("key")); //14
    }

    @Test
    public void stringTest2() {
        System.out.println(conn.append("new-string-key", "hello "));
        System.out.println(conn.append("new-string-key", "world!"));
        System.out.println(conn.substr("new-string-key", 3, 7));
        System.out.println(conn.setrange("new-string-key", 0, "H"));
        System.out.println(conn.setrange("new-string-key", 6, "W"));
        System.out.println(conn.get("new-string-key"));
        System.out.println(conn.setrange("new-string-key", 11, ", how are you?"));
        System.out.println(conn.get("new-string-key"));
    }

    @Test
    public void listTest() {
        System.out.println(conn.rpush("list-key", "last"));
        System.out.println(conn.lpush("list-key", "first"));
        System.out.println(conn.rpush("list-key", "new last"));
        System.out.println(conn.lrange("list-key", 0, -1));
        System.out.println(conn.lpop("list-key"));
        System.out.println(conn.lpop("list-key"));
        System.out.println(conn.lrange("list-key", 0, -1));
        System.out.println(conn.rpush("list-key", "a", "b", "c"));
        System.out.println(conn.lrange("list-key", 0, -1));
        System.out.println(conn.ltrim("list-key", 2, -1));
        System.out.println(conn.lrange("list-key", 0, -1));
    }

    @Test
    public void listTest2() {
        System.out.println(conn.rpush("list", "item1")); //list:item1
        System.out.println(conn.rpush("list", "item2")); //list:item1 item2
        System.out.println(conn.rpush("list2", "item3")); //list2:item3
        System.out.println(conn.brpoplpush("list2", "list", 1));//list:item3 item1 item2 list2:null
        System.out.println(conn.brpoplpush("list2", "list", 1));//list:item3 item1 item2 list2:null
        System.out.println(conn.lrange("list", 0, -1));//list:item3 item1 item2 list2:null
        System.out.println(conn.brpoplpush("list", "list2", 1)); //list:item3 item1 list2: item2
        System.out.println(conn.blpop(1, "list", "list2"));//list:item1 list2:item2
        System.out.println(conn.blpop(1, "list", "list2"));//list:null list2:item2
        System.out.println(conn.blpop(1, "list", "list2"));//list:null list2:null
        System.out.println(conn.blpop(1, "list", "list2"));//list:null list2:null
    }

    @Test
    public void setTest() {
        System.out.println(conn.sadd("set-key", "a", "b", "c"));
        System.out.println(conn.srem("set-key", "c", "d"));
        System.out.println(conn.srem("set-key", "c", "d"));
        System.out.println(conn.scard("set-key"));
        System.out.println(conn.smembers("set-key"));
        System.out.println(conn.smove("set-key", "set-key2", "a"));
        System.out.println(conn.smove("set-key", "set-key2", "c"));
        System.out.println(conn.smembers("set-key2"));
    }

    @Test
    public void setTest2() {
        System.out.println(conn.sadd("skey1", "a", "b", "c", "d"));
        System.out.println(conn.sadd("skey2", "c", "d", "e", "f"));
        System.out.println(conn.sdiff("skey1", "skey2")); //a,b
        System.out.println(conn.sinter("skey1", "skey2"));//c,d
        System.out.println(conn.sunion("skey1", "skey2"));//a,b,c,d,e,f
    }

    @Test
    public void hashTest() {
        HashMap<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        System.out.println(conn.hmset("hash-key", map));
        System.out.println(conn.hmget("hash-key", "k1", "k3")); //v1,v3
        System.out.println(conn.hlen("hash-key")); //3
        System.out.println(conn.hdel("hash-key", "k1", "k3"));
        System.out.println(conn.hmget("hash-key", "k1", "k2")); //null, v2
    }

    @Test
    public void hashTest2() {
        HashMap<String, String> map = new HashMap<>();
        map.put("short", "hello");
        map.put("long", "1000");
        System.out.println(conn.hmset("hash-key2", map));
        System.out.println(conn.hkeys("hash-key2")); //short,long
        System.out.println(conn.hexists("hash-key2", "num"));
        System.out.println(conn.hincrBy("hash-key2", "num", 1));
        System.out.println(conn.hexists("hash-key2", "num"));
    }

    @Test
    public void zsetTest() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("a", 3D);
        map.put("b", 2D);
        map.put("c", 1D);
        System.out.println(conn.zadd("zset-key", map));
        System.out.println(conn.zcard("zset-key"));
        System.out.println(conn.zincrby("zset-key", 3, "c")); //a:3,b:2,c:4
        System.out.println(conn.zscore("zset-key", "b")); //2
        System.out.println(conn.zrank("zset-key", "c")); //
        System.out.println(conn.zcount("zset-key", 0, 3));
        System.out.println(conn.zrem("zset-key", "b"));
        System.out.println(conn.zrangeWithScores("zset-key", 0, -1));
    }

    @Test
    public void zsetTest2() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("a", 1D);
        map.put("b", 2D);
        map.put("c", 3D);
        conn.zadd("zset-1", map);
        HashMap<String, Double> map2 = new HashMap<>();
        map2.put("b", 4D);
        map2.put("c", 1D);
        map2.put("d", 0D);
        conn.zadd("zset-2",map2);
        conn.zinterstore("zset-i", "zset-1", "zset-2");
        Set<Tuple> tupleSet = conn.zrangeWithScores("zset-i", 0, -1);
        Iterator<Tuple> iterator = tupleSet.iterator();
        while(iterator.hasNext()) {
            Tuple tuple = iterator.next();
            System.out.println("zset-i ==== " + tuple.getElement() + ": " + tuple.getScore());
        }

        ZParams zParams = new ZParams().aggregate(ZParams.Aggregate.MIN);
        conn.zunionstore("zset-u", zParams, "zset-1", "zset-2");
        Set<Tuple> tupleSet2 = conn.zrangeWithScores("zset-u", 0, -1);
        Iterator<Tuple> iterator2 = tupleSet2.iterator();
        while(iterator2.hasNext()) {
            Tuple tuple = iterator2.next();
            System.out.println("zset-u === " + tuple.getElement() + ": " + tuple.getScore());
        }

        conn.sadd("set-1", "a", "d");
        conn.zunionstore("zset-u2", "zset-1", "zset-2", "set-1");
        Set<Tuple> tupleSet3 = conn.zrangeWithScores("zset-u2", 0, -1);
        Iterator<Tuple> iterator3 = tupleSet3.iterator();
        while(iterator3.hasNext()) {
            Tuple tuple = iterator3.next();
            System.out.println("zset-u2 === " + tuple.getElement() + ": " + tuple.getScore());
        }
    }

    @Test
    public void sortTest() {
        conn.rpush("sort-input", "23", "15", "110", "7");
        System.out.println(conn.sort("sort-input"));
        SortingParams params = new SortingParams();
        System.out.println(conn.sort("sort-input", params.alpha()));

        conn.hset("d-7", "field", "5");
        conn.hset("d-15", "field", "1");
        conn.hset("d-23", "field", "9");
        conn.hset("d-110", "field", "3");
        SortingParams params2 = new SortingParams();
        params2.by("d-*->field");
        params2.get("d-*->field");
        System.out.println(conn.sort("sort-input", params2));
    }
}
