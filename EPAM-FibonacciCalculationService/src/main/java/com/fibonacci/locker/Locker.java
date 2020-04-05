package com.fibonacci.locker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Locker {
	private Map<Integer, Object> locks;
	private static final Logger logger = LogManager.getLogger();

	public Locker() {
		locks = new HashMap<>();
	}

	public Object getLock(Integer id) {
		logger.info("Getting locking object");
		if (!locks.containsKey(id)){
			locks.put(id, new Object());
		}
		return locks.get(id);
	}
}
