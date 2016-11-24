package cn.com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateNewPost {
	
	public static void main(String[] args) {
		
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "192.168.11.81");
		try(Jedis jedis = pool.getResource()) {
			Long postId = jedis.incr("posts:count");
			System.out.println(postId);
			String title = "我是测试标题";
			String content = "哈啊哈哈哈";
			String author = "hongxf";
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
			
//			List<String> src = new ArrayList<String>();
//			src.add(title);
//			src.add(content);
//			src.add(author);
//			src.add(time);
//			MessagePack msgpack = new MessagePack();
//			byte[] write = msgpack.write(src);
//			jedis.set("post:" + postId + ":data", write);
		}
		pool.destroy();
	}
}
