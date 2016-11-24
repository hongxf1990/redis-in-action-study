package cn.com.redis;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestClient {
	
	@Test
	public void basicTest() {
		Jedis jedis = new Jedis("192.168.11.81");
		System.out.println("Server is running: "+ jedis.ping());
		jedis.set("tutorial-name", "Redis tutorial");
		System.out.println("Stored string in redis:: " + jedis.get("tutorial-name"));
	}
	
	@Test
	public void poolTest() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.11.81");
		try(Jedis jedis = pool.getResource()) {
			jedis.set("foo", "bar");
			String foobar = jedis.get("foo");
			System.out.println(foobar);
		}
		pool.destroy();
	}
	
	@Test
	public void hsetTest() {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.11.81");
		try(Jedis jedis = pool.getResource()) {
//			jedis.hset("job", "salary", "15000");
			Long hsetnx = jedis.hsetnx("job", "salary", "1500");
			System.out.println(hsetnx);
			Map<String, String> map = new HashMap<String, String>();
			map.put("time", "9");
			map.put("position", "java");
			jedis.hmset("job", map);
			
			jedis.hincrBy("job", "salary", 100);
			
			Map<String, String> hgetAll = jedis.hgetAll("job");
			System.out.println(hgetAll);
		}
		pool.destroy();
	}
}
