package com.fibonacci.controller;

import com.fibonacci.model.Fibonacci;
import com.fibonacci.model.FibonacciDto;
import com.fibonacci.model.dao.AsyncCalculatedFibonacciValuesDao;
import com.fibonacci.model.services.CalculateFibonacciSequenceService;
import com.fibonacci.model.services.FibonacciServiceAccessManager;
import com.fibonacci.model.services.FibonacciStatisticService;
import com.fibonacci.validator.FibonacciValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/asyncCalculation")
public class FibonacciAsyncController {

	@Autowired
	private FibonacciValidator fibonacciValidator;
	@Autowired
	private FibonacciStatisticService fibonacciStatisticService;
	@Autowired
	private FibonacciServiceAccessManager accessManager;
	@Autowired
	private AsyncCalculatedFibonacciValuesDao asyncCalculatedFibonacciValuesDao;
	@Autowired
	private CalculateFibonacciSequenceService calculateFibonacciSequenceService;

	@RequestMapping(method = RequestMethod.GET)
	public String getForm(ModelMap model, HttpServletRequest request) {
		accessManager.registerAccess();
		String uuid = request.getParameter("uuid");
		if (uuid != null) {
			List<Fibonacci> result = asyncCalculatedFibonacciValuesDao.get(uuid);
			if (result != null) {
				model.addAttribute("fibonacci", result);
				return "asyncResult";
			}
		}
		model.put("fibonacciDto", new FibonacciDto());
		return "asyncCalculation";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String sendResult(FibonacciDto fibonacciDto, ModelMap model, BindingResult result) {
		accessManager.registerAccess();
		fibonacciValidator.validate(fibonacciDto, result);
		if (result.hasErrors()) {
			fibonacciStatisticService.incrementTotalBadRequestsQty();
			return "asyncCalculation";
		}
		String uuid = UUID.randomUUID().toString();
		CompletableFuture.runAsync(() -> {
			List<Fibonacci> list = Arrays.stream(calculateFibonacciSequenceService.parseIndexSequence(fibonacciDto))
					.parallel()
					.map(index -> calculateFibonacciSequenceService.calculate(index))
					.collect(Collectors.toList());
			asyncCalculatedFibonacciValuesDao.store(uuid, list);
			fibonacciStatisticService.updateStatistic(list);
			System.out.println("Message from CompletableFuture");
		});
		model.addAttribute("uuid", uuid);
		System.out.println(uuid);
		return "yourAsyncUuid";
	}


}
