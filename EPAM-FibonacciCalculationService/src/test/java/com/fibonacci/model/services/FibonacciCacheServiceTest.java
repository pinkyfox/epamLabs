package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.dao.FibonacciCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FibonacciCacheServiceTest {
	@Mock
	private FibonacciCache cache;
	private FibonacciCacheService service;

	private int key = 8;
	private String value = "21";
	private Fibonacci fibonacci = new Fibonacci(key, value);

	@Before
	public void init() {
		service = FibonacciCacheService.getInstance();
		service.setCache(cache);

		when(cache.get(key)).thenReturn(value);
	}

	@Test
	public void getFibonacciObjectWhenCallGet() {
		assertEquals(fibonacci, service.get(key));
	}

	@Test
	public void callStoreWhenCallSave() {
		service.save(fibonacci);
		verify(cache).store(key, value);
	}
}
