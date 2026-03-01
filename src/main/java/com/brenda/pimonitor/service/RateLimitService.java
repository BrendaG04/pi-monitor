package com.brenda.pimonitor.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
	Rate Limiting ~ prevents brute force attacks-too many passwords.
*/
@Service
public class RateLimitService {

	//will store buckets per ip address
	private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

	//creates bucket for an IP
	public Bucket resolveBucket(String key) {
		return cache.computeIfAbsent(key, k -> createNewBucket());
	}

	//rate limit : 5 attemps per minute
	private Bucket createNewBucket() {
		Bandwidth limit = Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1)));
		return Bucket.builder()
				.addLimit(limit)
				.build();
	}
}
