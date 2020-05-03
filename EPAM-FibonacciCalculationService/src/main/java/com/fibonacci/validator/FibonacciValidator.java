package com.fibonacci.validator;


import com.fibonacci.model.FibonacciDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class FibonacciValidator implements Validator {

	private static final Logger logger = LogManager.getLogger();
	@Override
	public boolean supports(Class<?> aClass) {
		return FibonacciDto.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		logger.info("Validating user input");
		FibonacciDto fibonacci = (FibonacciDto) o;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "index", "index.empty", "index must not be empty.");
		Integer[] indexes = getIndexes(fibonacci.getIndex());
		if (indexes == null) {
			logger.error("Invalid input");
			errors.rejectValue("index", "index.invalidInput", "Index(-es) should contains only '0'-'9' symbols and '-'. If " +
					"you wanna calculate a sequence of indexes than indexes should be split by ', '. For example: 1, 2, 3, 4");
		} else {
			for (Integer index : indexes) {
				if (index < 0) {
					logger.error("Invalid input");
					errors.rejectValue("index", "index.tooSmall", "Index(-es) should be equals or greater than 0");
				}
			}
		}
	}

	private Integer[] getIndexes(String string) {
		logger.info("Converting user input from string to int val");
		try {
			String[] indexes = string.split(", ");
			Integer[] result = new Integer[indexes.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = Integer.parseInt(indexes[i]);
			}
			return result;
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
