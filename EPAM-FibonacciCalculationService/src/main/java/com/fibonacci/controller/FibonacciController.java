package com.fibonacci.controller;

import com.fibonacci.model.services.CalculateFibonacciSequenceService;
import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.FibonacciDto;
import com.fibonacci.model.services.FibonacciServiceAccessManager;
import com.fibonacci.validator.FibonacciValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/calculation")
public class FibonacciController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private FibonacciValidator fibonacciValidator;
	@Autowired
	private FibonacciServiceAccessManager accessManager;
	@Autowired
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService;

	@RequestMapping(method = RequestMethod.GET)
	public String getForm(ModelMap model) {
		logger.info("Getting fibonacci form");
		accessManager.registerAccess();
		model.put("fibonacciDto", new FibonacciDto());
		return "calculation";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String sendResult(FibonacciDto fibonacciDto, ModelMap model, BindingResult result) {
		logger.info("Getting fibonacci calculations");
		accessManager.registerAccess();
		fibonacciValidator.validate(fibonacciDto, result);
		if (result.hasErrors()) {
			logger.warn("Invalid input in fibonacci form. Sending response to the client");
			return "calculation";
		}
		List<Fibonacci> list =  Arrays.stream(calculateFibonacciSequenceService.parseIndexSequence(fibonacciDto))
				.parallel()
				.map(index -> calculateFibonacciSequenceService.calculate(index))
				.collect(Collectors.toList());
		model.addAttribute("fibonacci", list);
		return "result";
	}
}