package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.cache.FibonacciCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.lang.StrictMath.sqrt;

@Service("CalculateFibonacciSequenceService")
public class CalculateFibonacciSequenceService {
	private static final Logger logger = LogManager.getLogger();
	@Autowired
	private FibonacciCache cache;

	public CalculateFibonacciSequenceService() {
	}

	public void setCacheService(FibonacciCache cache) {
		logger.info("Setting up cache service");
		this.cache = cache;
	}

	public Fibonacci calculate(int indexFibonacciNumber) {
		logger.info("Check up cache");
		Fibonacci fibonacciResult = cache.get(indexFibonacciNumber);
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
			result = result.divide(goldenSearchRatio, new MathContext(100));
			logger.info("Stop calculations");
			fibonacciResult = new Fibonacci(indexFibonacciNumber, result.setScale(0, RoundingMode.HALF_UP).toPlainString());
			cache.store(indexFibonacciNumber, fibonacciResult);
		}
		return fibonacciResult;
	}

}
