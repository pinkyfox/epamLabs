package com.fibonacci.model.cache;

import com.fibonacci.model.Fibonacci;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import static org.junit.Assert.assertEquals;

public class FibonacciCacheTest {
	private static FibonacciCache fibonacciCache;
	private static ConcurrentHashMap<Integer, Fibonacci> testData;

	@Before
	public void init() {
		fibonacciCache = new FibonacciCache();
		testData = new ConcurrentHashMap<>();
		testData.put(0, new Fibonacci(0, "10"));
		testData.put(1, new Fibonacci(1, "21"));
		testData.put(2, new Fibonacci(2, "32"));
		testData.put(3, new Fibonacci(3, "43"));
		testData.put(4, new Fibonacci(5, "54"));

		fibonacciCache.setCache(testData);
	}

	@Test
	public void getSizeWhenCallSize() {
		assertEquals(testData.size(), fibonacciCache.size());
	}

	@Test
	public void getPreviousCalculationByKeyWhenCallGet() {
		Fibonacci expected[] = {  new Fibonacci(0, "10"),
				new Fibonacci(1, "21"),
				new Fibonacci(2, "32"),
				new Fibonacci(3, "43"),
				new Fibonacci(5, "54"),
				null
		};

		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], fibonacciCache.get(i));
		}
	}

	@Test
	public void addingCalculationToCacheWhenCallStore() {
		testData.put(6, new Fibonacci(6, "65"));
		testData.put(7, new Fibonacci(7, "76"));

		fibonacciCache.store(6, new Fibonacci(6, "65"));
		fibonacciCache.store(7, new Fibonacci(7, "76"));

		Set<Integer> keys = testData.keySet();
		ConcurrentHashMap<Integer, Fibonacci> actualResult = fibonacciCache.getCache();

		assertEquals(testData.size(), actualResult.size());
		for (Integer key : keys) {
			assertEquals(testData.get(key), actualResult.get(key));
		}
	}

	@Test
	public void deleteCalculationWhenCallRemove() {
		testData.remove(0);
		testData.remove(3);

		fibonacciCache.remove(0);
		fibonacciCache.remove(3);

		Set<Integer> keys = testData.keySet();
		ConcurrentHashMap<Integer, Fibonacci> actualResult = fibonacciCache.getCache();

		assertEquals(testData.size(), actualResult.size());
		for (Integer key : keys) {
			assertEquals(testData.get(key), actualResult.get(key));
		}
	}

}
