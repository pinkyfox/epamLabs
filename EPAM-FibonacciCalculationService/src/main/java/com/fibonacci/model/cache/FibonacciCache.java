package com.fibonacci.model.cache;

import com.fibonacci.model.Fibonacci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service("FibonacciCache")
public class FibonacciCache {
	private static final Logger logger = LogManager.getLogger();

	private static ConcurrentHashMap<Integer, Fibonacci> objects;
	private static ConcurrentHashMap<Integer, Long> expire;
	private long expirationDate = 120_000;
	private static ExecutorService threads;

	public FibonacciCache() {
		objects = new ConcurrentHashMap<>();
		expire = new ConcurrentHashMap<>();
		threads = Executors.newFixedThreadPool(128);
		Executors.newScheduledThreadPool(2).scheduleWithFixedDelay(this.removeExpired(),
				expirationDate / 2,
				expirationDate,
				TimeUnit.MILLISECONDS);
	}

	public int size() {
		logger.info("Getting size of cache");
		return objects.size();
	}

	public Fibonacci get(int key) {
		logger.info("Transfer data form cache");
		return objects.getOrDefault(key, null);
	}

	public void setCache(ConcurrentHashMap<Integer, Fibonacci> previousCalculation) {
		logger.info("Setting up new cache");
		FibonacciCache.objects.clear();
		Set<Entry<Integer, Fibonacci>> set = previousCalculation.entrySet();
		for(Entry<Integer, Fibonacci> entry : set) {
			FibonacciCache.objects.put(entry.getKey(), entry.getValue());
			FibonacciCache.expire.put((entry.getKey()), System.currentTimeMillis() + expirationDate);
		}
	}

	public ConcurrentHashMap<Integer, Fibonacci> getCache() {
		logger.info("Getting cache");
		return FibonacciCache.objects;
	}

	public void store(int key, Fibonacci value) {
		logger.info("Load data to cache");
		objects.put(key, value);
		expire.put(key, System.currentTimeMillis() + expirationDate);
	}

	public void remove(int key) {
		logger.info("Removing data from cache");
		objects.remove(key);
		expire.remove(key);
	}

	private final Runnable removeExpired() {
		return () -> {
			for (final Integer key : expire.keySet()) {
				if (System.currentTimeMillis() > expire.get(key)) {
					threads.execute(createRemoveRunnable(key));
				}
			}
		};
	}

	private final Runnable createRemoveRunnable(final Integer name) {
		return () -> {
			objects.remove(name);
			expire.remove(name);
		};
	}

}
