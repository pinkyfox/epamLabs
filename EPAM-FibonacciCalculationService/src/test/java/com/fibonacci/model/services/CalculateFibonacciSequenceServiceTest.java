package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
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
	private FibonacciCacheService cacheService;
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService;

	private HashMap<Integer, Integer> testData;
	private int key = 9;
	private String value = "34";

	@Before
	public void init() {
		calculateFibonacciSequenceService = CalculateFibonacciSequenceService.getInstance();
		testData = new HashMap<>();
		testData.put(0, 0);
		testData.put(1, 1);
		testData.put(2, 1);
		testData.put(3, 2);
		testData.put(4, 3);
		testData.put(5, 5);
		testData.put(6, 8);
		testData.put(7, 13);
		testData.put(8, 21);
	}

	@Test
	public void fibonacciNumberWhenCalculate() {
		for (Integer value : testData.keySet()) {
			assertEquals(testData.get(value).toString(), calculateFibonacciSequenceService.calculate(value).getValue());
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
		calculateFibonacciSequenceService.setCacheService(cacheService);
		calculateFibonacciSequenceService.calculate(key);
		verify(cacheService).get(key);
	}

	@Test
	public void ifCacheReturnNullThanStoreCalculationToCacheWhenCallCalculate() {
		calculateFibonacciSequenceService.setCacheService(cacheService);
		calculateFibonacciSequenceService.calculate(key);
		verify(cacheService).save(new Fibonacci(key, value));
	}
}