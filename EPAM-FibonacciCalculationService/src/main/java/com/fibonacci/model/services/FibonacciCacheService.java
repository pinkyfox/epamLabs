package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.dao.FibonacciCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;

public class FibonacciCacheService {

	private static final int CACHE_SIZE = 100;
	private static final Logger logger = LogManager.getLogger();

	private static FibonacciCacheService instance;
	private static FibonacciCache cache;
	private static Stack<Integer> keys;

	public static synchronized FibonacciCacheService getInstance() {
		logger.info("Getting instance of FibonacciCacheService class");
		if (instance == null) {
			instance = new FibonacciCacheService();
			cache = FibonacciCache.getInstance();
			keys = new Stack<>();
		}
		return instance;
	}

	public void setCache(FibonacciCache cache) {
		logger.info("Setting up cache");
		FibonacciCacheService.cache = cache;
	}

	public Fibonacci get(int key) {
		logger.info("Transfer data form cache and wrapped it");
		String value = cache.get(key);
		return value == null ? null :
				new Fibonacci(key, value);
	}

	public void save(Fibonacci fibonacci) {
		logger.info("Unboxing data and load it to cache");
		if (keys.size() == CACHE_SIZE) {
			cache.remove(keys.pop());
		}
		keys.push(fibonacci.getIndex());
		cache.store(fibonacci.getIndex(), fibonacci.getValue());
	}

	private FibonacciCacheService() {

	}
}
