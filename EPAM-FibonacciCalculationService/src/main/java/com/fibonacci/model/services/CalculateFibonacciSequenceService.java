package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.FibonacciDto;
import com.fibonacci.model.cache.FibonacciCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static java.lang.StrictMath.sqrt;

@Service("CalculateFibonacciSequenceService")
public class CalculateFibonacciSequenceService {
	@Autowired
	private FibonacciCache cache;

	public CalculateFibonacciSequenceService() {
	}

	public void setCacheService(FibonacciCache cache) {
		this.cache = cache;
	}

	public Fibonacci calculate(int indexFibonacciNumber) {
		Fibonacci fibonacciResult = cache.get(indexFibonacciNumber);
		if (fibonacciResult != null) {
			return fibonacciResult;
		} else {
			BigDecimal result = new BigDecimal(0);
			BigDecimal goldenSearchRatio = new BigDecimal((1 + sqrt(5)) / 2);
			result = result.add(goldenSearchRatio.pow(indexFibonacciNumber));
			goldenSearchRatio = goldenSearchRatio.negate();
			int negativeIndexFibonacciNumber = (-1) * indexFibonacciNumber;
			result = result.subtract(goldenSearchRatio.pow(negativeIndexFibonacciNumber, new MathContext(100000)));
			goldenSearchRatio = goldenSearchRatio.multiply(new BigDecimal(-2));
			goldenSearchRatio = goldenSearchRatio.subtract(new BigDecimal(1));
			result = result.divide(goldenSearchRatio, new MathContext(100));
			fibonacciResult = new Fibonacci(indexFibonacciNumber, result.setScale(0, RoundingMode.HALF_UP).toPlainString());
			cache.store(indexFibonacciNumber, fibonacciResult);
		}
		return fibonacciResult;
	}

	public Integer[] parseIndexSequence(FibonacciDto fibonacciDto) {
		String[] stringIndexes = fibonacciDto.getIndex().split(", ");
		Integer[] integerIndexes = new Integer[stringIndexes.length];
		for (int i =0; i < integerIndexes.length; i++) {
			integerIndexes[i] = Integer.parseInt(stringIndexes[i]);
		}
		return integerIndexes;
	}

}
