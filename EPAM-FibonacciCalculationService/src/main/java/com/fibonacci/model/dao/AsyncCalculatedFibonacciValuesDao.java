package com.fibonacci.model.dao;

import com.fibonacci.model.Fibonacci;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


@Service("AsyncCalculatedFibonacciValuesDao")
public class AsyncCalculatedFibonacciValuesDao {
	private static HashMap<String, List<Fibonacci>> storage;
	private static ReentrantLock mutex;

	public AsyncCalculatedFibonacciValuesDao() {
		storage = new HashMap<>();
		mutex = new ReentrantLock();
	}

	public List<Fibonacci> get(String uuid) {
		return storage.getOrDefault(uuid, null);
	}

	public void store(String key, List<Fibonacci> value) {
		try {
			mutex.lock();
			storage.put(key, value);
		} finally {
			mutex.unlock();
		}
	}
}
