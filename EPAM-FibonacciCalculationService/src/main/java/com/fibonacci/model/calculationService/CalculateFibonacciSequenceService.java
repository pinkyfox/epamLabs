package com.fibonacci.model.calculationService;

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

	public static CalculateFibonacciSequenceService getInstance() {
		if (instance == null) {
			instance = new CalculateFibonacciSequenceService();
		}
		return instance;
	}

	public Fibonacci calculate(int indexFibonacciNumber) {
		logger.info("Start calculations");
		BigDecimal result = new BigDecimal(0); // 2
		BigDecimal goldenSearchRatio = new BigDecimal((1 + sqrt(5)) / 2);
		result = result.add(goldenSearchRatio.pow(indexFibonacciNumber)); // fi^n
		goldenSearchRatio = goldenSearchRatio.negate();
		int negativeIndexFibonacciNumber = (-1) * indexFibonacciNumber;
		result = result.subtract(goldenSearchRatio.pow(negativeIndexFibonacciNumber, new MathContext(100000))); // fi^n - (-fi) ^ (-n)
		goldenSearchRatio = goldenSearchRatio.multiply(new BigDecimal(-2));
		goldenSearchRatio = goldenSearchRatio.subtract(new BigDecimal(1));
		result = result.divide(goldenSearchRatio, new MathContext(100000));
		logger.info("Stop calculations");
		return new Fibonacci(indexFibonacciNumber, result.setScale(0, RoundingMode.HALF_UP).toPlainString());
	}

	private CalculateFibonacciSequenceService() {
		//NOP
	}
}
