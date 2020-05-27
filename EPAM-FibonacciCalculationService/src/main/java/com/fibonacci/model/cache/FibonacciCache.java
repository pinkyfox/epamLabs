package com.fibonacci.model.cache;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.dao.CalculatedFibonacciValuesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service("FibonacciCache")
public class FibonacciCache {

	@Autowired
	private CalculatedFibonacciValuesDao fibonacciDao;

	private static ConcurrentHashMap<Integer, Fibonacci> objects;
	private static ConcurrentHashMap<Integer, Long> expire;
	private long expirationDate = 600_000;
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
		return objects.size();
	}

	public Fibonacci get(int key) {
		return objects.getOrDefault(key, null);
	}

	public void setCache(ConcurrentHashMap<Integer, Fibonacci> previousCalculation) {
		FibonacciCache.objects.clear();
		Set<Entry<Integer, Fibonacci>> set = previousCalculation.entrySet();
		for (Entry<Integer, Fibonacci> entry : set) {
			FibonacciCache.objects.put(entry.getKey(), entry.getValue());
			FibonacciCache.expire.put(entry.getKey(), System.currentTimeMillis() + expirationDate);
		}
	}

	public ConcurrentHashMap<Integer, Fibonacci> getCache() {
		return FibonacciCache.objects;
	}

	public void store(int key, Fibonacci value) {
		if (!objects.containsKey(value.getIndex())) {
			fibonacciDao.add(value);
		}
		objects.put(key, value);
		expire.put(key, System.currentTimeMillis() + expirationDate);
	}

	public void remove(int key) {
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

	@EventListener(ApplicationReadyEvent.class)
	private void setUpCacheFromDataBase() {
		for (Fibonacci fibonacci : fibonacciDao.getAllThatLessOrEqual(250)) {
			FibonacciCache.objects.put(fibonacci.getIndex(), fibonacci);
			FibonacciCache.expire.put(fibonacci.getIndex(), System.currentTimeMillis() + expirationDate);
		}
	}
}
