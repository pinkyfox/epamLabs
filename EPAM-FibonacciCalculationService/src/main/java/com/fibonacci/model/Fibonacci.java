package com.fibonacci.model;

import java.util.Objects;

public class Fibonacci {
	private int index;
	private String value;

	public Fibonacci() {

	}

	public Fibonacci(int index, String value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int content) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isEmpty() {
		return value == null ? true :
				false;
	}

	@Override
	public String toString() {
		return "Fibonacci{" +
				"index=" + index +
				", value='" + value + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Fibonacci fibonacci = (Fibonacci) o;
		return index == fibonacci.index;
	}

	@Override
	public int hashCode() {
		return Objects.hash(index);
	}
}