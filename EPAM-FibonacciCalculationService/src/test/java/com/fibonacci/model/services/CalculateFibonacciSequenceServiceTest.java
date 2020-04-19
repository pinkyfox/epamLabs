package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.cache.FibonacciCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CalculateFibonacciSequenceServiceTest {
	@Mock
	private FibonacciCache cache;
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService;

	private HashMap<Integer, Fibonacci> testData;
	private int key = 9;
	private String value = "34";

	@Before
	public void init() {
		calculateFibonacciSequenceService = new CalculateFibonacciSequenceService();
		testData = new HashMap<>();
		testData.put(1, new Fibonacci(1, "1"));
		testData.put(2, new Fibonacci(2, "1"));
		testData.put(3, new Fibonacci(3, "2"));
		testData.put(4, new Fibonacci(4, "3"));
		testData.put(5, new Fibonacci(5, "5"));
		testData.put(6, new Fibonacci(6, "8"));
		testData.put(7, new Fibonacci(7, "13"));
		testData.put(8, new Fibonacci(8, "21"));
	}

	@Test
	public void fibonacciNumberWhenCalculate() {
		for (Integer key : testData.keySet()) {
			assertEquals(testData.get(key), calculateFibonacciSequenceService.calculate(key));
		}
	}

	@Test(expected = Exception.class)
	public void hasErrorsWhenCalculate() {
		for (Integer value : testData.keySet()) {
			calculateFibonacciSequenceService.calculate(value * (-1));
		}
	}

	@Test
	public void checkCacheBeforeCalculatingWhenCallCalculate() {
		calculateFibonacciSequenceService.setCacheService(cache);
		calculateFibonacciSequenceService.calculate(key);
		verify(cache).get(key);
	}

	@Test
	public void ifCacheReturnNullThanStoreCalculationToCacheWhenCallCalculate() {
		calculateFibonacciSequenceService.setCacheService(cache);
		calculateFibonacciSequenceService.calculate(key);
		verify(cache).store(key, new Fibonacci(key, value));
	}
}
