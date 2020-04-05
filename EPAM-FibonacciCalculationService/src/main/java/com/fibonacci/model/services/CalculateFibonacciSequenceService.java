package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.lang.StrictMath.sqrt;


public class CalculateFibonacciSequenceService {
	private static final Logger logger = LogManager.getLogger();
	private static CalculateFibonacciSequenceService instance;
	private static FibonacciCacheService cacheService;

	public static synchronized CalculateFibonacciSequenceService getInstance() {
		if (instance == null) {
			instance = new CalculateFibonacciSequenceService();
			cacheService = FibonacciCacheService.getInstance();
		}
		return instance;
	}

	public void setCacheService(FibonacciCacheService cacheService) {
		logger.info("Setting up cache service");
		CalculateFibonacciSequenceService.cacheService = cacheService;
	}

	public Fibonacci calculate(int indexFibonacciNumber) {
		logger.info("Check up cache");
		Fibonacci fibonacciResult = cacheService.get(indexFibonacciNumber);
		if (fibonacciResult != null) {
			logger.info("Transfer result from cache");
			return fibonacciResult;
		} else {
			logger.info("Cache doesn't contain result");
			logger.info("Start calculations");
			BigDecimal result = new BigDecimal(0);
			BigDecimal goldenSearchRatio = new BigDecimal((1 + sqrt(5)) / 2);
			result = result.add(goldenSearchRatio.pow(indexFibonacciNumber));
			goldenSearchRatio = goldenSearchRatio.negate();
			int negativeIndexFibonacciNumber = (-1) * indexFibonacciNumber;
			result = result.subtract(goldenSearchRatio.pow(negativeIndexFibonacciNumber, new MathContext(100000)));
			goldenSearchRatio = goldenSearchRatio.multiply(new BigDecimal(-2));
			goldenSearchRatio = goldenSearchRatio.subtract(new BigDecimal(1));
			result = result.divide(goldenSearchRatio, new MathContext(100000));
			logger.info("Stop calculations");
			fibonacciResult = new Fibonacci(indexFibonacciNumber, result.setScale(0, RoundingMode.HALF_UP).toPlainString());
			cacheService.save(fibonacciResult);
		}
		return fibonacciResult;
	}

	private CalculateFibonacciSequenceService() {
		//NOP
	}
}
