package com.fibonacci.controller;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.calculationService.CalculateFibonacciSequenceService;
import com.fibonacci.model.dto.FibonacciDto;
import com.fibonacci.validator.FibonacciValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping("/calculation")
public class FibonacciController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private FibonacciValidator fibonacciValidator;
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService = CalculateFibonacciSequenceService.getInstance();

	@RequestMapping(method = RequestMethod.GET)
	public String getForm(ModelMap model) {
		logger.info("Getting fibonacci form");
		model.put("fibonacciDto", new FibonacciDto());
		return "calculation";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String sendResult(FibonacciDto fibonacciDto, ModelMap model, BindingResult result) {
		logger.info("Getting fibonacci calculations");
		fibonacciValidator.validate(fibonacciDto, result);
		if (result.hasErrors()) {
			logger.warn("Invalid input in fibonacci form. Sending response to the client");
			return "calculation";
		}
		Fibonacci fibonacci = calculateFibonacciSequenceService.calculate(Integer.parseInt(fibonacciDto.getIndex()));
		model.addAttribute("fibonacci", fibonacci);
		return "result";
	}
}