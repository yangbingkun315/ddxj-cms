package net.zn.ddxj.config.shiro.redis;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

public class StringRedisSerializer implements RedisSerializer<String> {
	private final Charset charset;

	public StringRedisSerializer() {
		this(Charset.forName("UTF-8"));
	}

	public StringRedisSerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	public String deserialize(byte[] bytes) {
		return new String(bytes, this.charset);
	}

	public byte[] serialize(String string) {
		return ((string == null) ? null : string.getBytes(this.charset));
	}
}