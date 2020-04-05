package com.fibonacci.model.dao;

import com.fibonacci.locker.Locker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FibonacciCache {

	private static final Logger logger = LogManager.getLogger();

	private static FibonacciCache instance;
	private static Locker locker;
	private static Map<Integer, String> previousCalculation;

	public static synchronized FibonacciCache getInstance() {
		logger.info("Getting instance of FibonacciCache class");
		if (instance == null) {
			instance = new FibonacciCache();
			locker = new Locker();
			previousCalculation = new LinkedHashMap<>();
		}
		return instance;
	}

	public int size() {
		logger.info("Getting size of cache");
		return previousCalculation.size();
	}

	public String get(int key) {
		logger.info("Transfer data form cache");
		return previousCalculation.getOrDefault(key, null);
	}

	public void setCache(Map<Integer, String> previousCalculation) {
		logger.info("Setting up new cache");
		FibonacciCache.previousCalculation.clear();
		Set<Entry<Integer, String>> set = previousCalculation.entrySet();
		for(Entry<Integer, String> entry : set) {
			FibonacciCache.previousCalculation.put(entry.getKey(), entry.getValue());
		}
	}

	public Map<Integer, String> getCache() {
		logger.info("Getting cache");
		return FibonacciCache.previousCalculation;
	}

	public void store(int key, String value) {
		logger.info("Load data to cache");
		synchronized (locker.getLock(key)) {
			previousCalculation.put(key, value);
		}
	}

	public void remove(int key) {
		logger.info("Removing data from cache");
		synchronized (locker.getLock(key)) {
			previousCalculation.remove(key);
		}
	}

	private FibonacciCache() {

	}

}
