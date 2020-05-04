package com.fibonacci.model.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service("FibonacciServiceAccessManager")
public class FibonacciServiceAccessManager {
	private static final Logger logger = LogManager.getLogger();

	private static long totalAccessQuantity;
	private ReentrantLock mutex;

	public FibonacciServiceAccessManager() {
		mutex = new ReentrantLock();
	}

	public void registerAccess() {
		try {
			mutex.lock();
			totalAccessQuantity += 1;
			logger.info("totalAccessQuantity = " + totalAccessQuantity);
		} finally {
			mutex.unlock();
		}
	}

	public long getTotalAccessQuantity() {
		return totalAccessQuantity;
	}
}
