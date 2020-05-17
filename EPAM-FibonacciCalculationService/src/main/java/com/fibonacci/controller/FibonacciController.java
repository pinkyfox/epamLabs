package com.fibonacci.controller;

import com.fibonacci.model.services.CalculateFibonacciSequenceService;
import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.FibonacciDto;
import com.fibonacci.model.services.FibonacciServiceAccessManager;
import com.fibonacci.model.services.FibonacciStatisticService;
import com.fibonacci.validator.FibonacciValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/calculation")
public class FibonacciController {
	@Autowired
	private FibonacciValidator fibonacciValidator;
	@Autowired
	private FibonacciStatisticService fibonacciStatisticService;
	@Autowired
	private FibonacciServiceAccessManager accessManager;
	@Autowired
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService;

	@RequestMapping(method = RequestMethod.GET)
	public String getForm(ModelMap model) {
		accessManager.registerAccess();
		model.put("fibonacciDto", new FibonacciDto());
		return "calculation";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String sendResult(FibonacciDto fibonacciDto, ModelMap model, BindingResult result) {
		accessManager.registerAccess();
		fibonacciValidator.validate(fibonacciDto, result);
		if (result.hasErrors()) {
			fibonacciStatisticService.incrementTotalBadRequestsQty();
			return "calculation";
		}
		List<Fibonacci> list = Arrays.stream(calculateFibonacciSequenceService.parseIndexSequence(fibonacciDto))
				.parallel()
				.map(index -> calculateFibonacciSequenceService.calculate(index))
				.collect(Collectors.toList());
		fibonacciStatisticService.updateStatistic(list);
		model.addAttribute("fibonacci", list);
		return "result";
	}
}