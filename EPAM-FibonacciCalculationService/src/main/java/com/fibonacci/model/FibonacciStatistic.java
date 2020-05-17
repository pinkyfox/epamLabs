package com.fibonacci.model;

import java.util.List;

public class FibonacciStatistic {
	private long totalRequestsQty;
	private long totalBadRequestsQty;
	private Fibonacci maxCalculatedValue;
	private Fibonacci minCalculatedValue;
	private List<Fibonacci> largestCalculatedSequence;

	public FibonacciStatistic() {
		//NOP
	}

	public FibonacciStatistic(long totalRequestsQty, long totalBadRequestsQty,
	                          Fibonacci maxCalculatedValue, Fibonacci minCalculatedValue,
	                          List<Fibonacci> largestCalculatedSequence) {
		this.totalRequestsQty = totalRequestsQty;
		this.totalBadRequestsQty = totalBadRequestsQty;
		this.maxCalculatedValue = maxCalculatedValue;
		this.minCalculatedValue = minCalculatedValue;
		this.largestCalculatedSequence = largestCalculatedSequence;
	}

	public long getTotalRequestsQty() {
		return totalRequestsQty;
	}

	public void setTotalRequestsQty(long totalRequestsQty) {
		this.totalRequestsQty = totalRequestsQty;
	}

	public long getTotalBadRequestsQty() {
		return totalBadRequestsQty;
	}

	public void setTotalBadRequestsQty(long totalBadRequestsQty) {
		this.totalBadRequestsQty = totalBadRequestsQty;
	}

	public Fibonacci getMaxCalculatedValue() {
		return maxCalculatedValue;
	}

	public void setMaxCalculatedValue(Fibonacci maxCalculatedValue) {
		this.maxCalculatedValue = maxCalculatedValue;
	}

	public Fibonacci getMinCalculatedValue() {
		return minCalculatedValue;
	}

	public void setMinCalculatedValue(Fibonacci minCalculatedValue) {
		this.minCalculatedValue = minCalculatedValue;
	}

	public List<Fibonacci> getLargestCalculatedSequence() {
		return largestCalculatedSequence;
	}

	public void setLargestCalculatedSequence(List<Fibonacci> largestCalculatedSequence) {
		this.largestCalculatedSequence = largestCalculatedSequence;
	}
}
