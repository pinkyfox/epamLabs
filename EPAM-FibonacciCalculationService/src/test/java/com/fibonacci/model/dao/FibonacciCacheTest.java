package com.fibonacci.model.dao;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FibonacciCacheTest {
	private static FibonacciCache fibonacciCache;
	private static Map<Integer, String> testData;

	@Before
	public void init() {
		fibonacciCache = FibonacciCache.getInstance();
		testData = new HashMap<>();
		testData.put(0, "10");
		testData.put(1, "21");
		testData.put(2, "32");
		testData.put(3, "43");
		testData.put(4, "54");
		fibonacciCache.setCache(testData);
	}

	@Test
	public void getSizeWhenCallSize() {
		assertEquals(testData.size(), fibonacciCache.size());
	}

	@Test
	public void getPreviousCalculationByKeyWhenCallGet() {
		String expected[] = { "10", "21", "32", "43", "54", null };
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], fibonacciCache.get(i));
		}
	}

	@Test
	public void addingCalculationToCacheWhenCallStore() {
		testData.put(5, "65");
		testData.put(6, "76");

		fibonacciCache.store(5, "65");
		fibonacciCache.store(6, "76");

		Set<Integer> keys = testData.keySet();
		Map<Integer, String> actualResult = fibonacciCache.getCache();

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
		Map<Integer, String> actualResult = fibonacciCache.getCache();

		assertEquals(testData.size(), actualResult.size());
		for (Integer key : keys) {
			assertEquals(testData.get(key), actualResult.get(key));
		}
	}

}
