package com.fibonacci.validator;


import com.fibonacci.model.dto.FibonacciDto;
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
		String intValue = fibonacci.getIndex();
		if (getIntegerInstance(intValue) == null) {
			logger.error("Invalid input");
			errors.rejectValue("index", "index.invalidInput", "Index should contains only '0'-'9' symbols and '-'");
		} else if (getIntegerInstance(intValue) < 0) {
			logger.error("Invalid input");
			errors.rejectValue("index", "index.tooSmall", "Index should be equals or greater than 0");
		}
	}

	private Integer getIntegerInstance(String string) {
		logger.info("Converting user input from string to int val");
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
