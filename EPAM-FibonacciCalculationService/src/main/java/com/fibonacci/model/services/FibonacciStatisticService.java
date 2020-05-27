package com.fibonacci.model.services;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.FibonacciStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Service("FibonacciStatisticService")
public class FibonacciStatisticService {
	private ReentrantLock mutex;

	@Autowired
	private FibonacciServiceAccessManager totalRequestsQty;
	private AtomicLong totalBadRequestsQty;
	private Fibonacci maxCalculatedValue;
	private Fibonacci minCalculatedValue;
	private ArrayList<Fibonacci> largestCalculatedSequence;

	public FibonacciStatisticService() {
		mutex = new ReentrantLock();
		this.totalBadRequestsQty = new AtomicLong(0);
		this.maxCalculatedValue = new Fibonacci(0, null);
		this.minCalculatedValue = new Fibonacci(Integer.MAX_VALUE, null);
		this.largestCalculatedSequence = new ArrayList<>();
	}

	public void incrementTotalBadRequestsQty() {
		totalBadRequestsQty.incrementAndGet();
	}

	public void updateStatistic(List<Fibonacci> fibonacciList) {
		try {
			mutex.lock();

			setLargestCalculatedSequence(fibonacciList);

			fibonacciList.stream().forEach(fibonacci -> {
						setMaxCalculatedValue(fibonacci);
						setMinCalculatedValue(fibonacci);
			});

		} finally {
			mutex.unlock();
		}
	}

	private void setMaxCalculatedValue(Fibonacci fibonacci) {
		if (fibonacci.getIndex() >= maxCalculatedValue.getIndex()) {
			maxCalculatedValue = fibonacci;
		}
	}

	private void setMinCalculatedValue(Fibonacci fibonacci) {
		if (fibonacci.getIndex() <= minCalculatedValue.getIndex()) {
			minCalculatedValue = fibonacci;
		}
	}

	private void setLargestCalculatedSequence(List<Fibonacci> fibonacciList) {
		if (fibonacciList.size() > largestCalculatedSequence.size()) {
			largestCalculatedSequence.clear();
			largestCalculatedSequence.addAll(fibonacciList);
		}
	}

	public FibonacciStatistic getFibonacciStatistic() {
		return new FibonacciStatistic(totalRequestsQty.getTotalAccessQuantity(),
				totalBadRequestsQty.get(),
				maxCalculatedValue,
				minCalculatedValue,
				largestCalculatedSequence);
	}
}
