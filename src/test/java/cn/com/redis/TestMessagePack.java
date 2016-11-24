package cn.com.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.msgpack.MessagePack;

public class TestMessagePack {

	@Test
	public void testSeri() throws IOException {
		List<String> src = new ArrayList<String>();
		src.add("msgpack");
		src.add("kumofs");
		src.add("viver");
		
		MessagePack msgpack = new MessagePack();
		byte[] write = msgpack.write(src);
		System.out.println(write);
	}
}
