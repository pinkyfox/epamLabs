package com.fibonacci.controller;

import com.fibonacci.model.services.FibonacciServiceAccessManager;
import com.fibonacci.model.services.FibonacciStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/statistic")
public class FibonacciStatisticController {
	@Autowired
	private FibonacciStatisticService statisticService;
	@Autowired
	private FibonacciServiceAccessManager accessManager;
	@RequestMapping(method = RequestMethod.GET)
	public String getStatistic(ModelMap model) {
		accessManager.registerAccess();
		Date currentTime = new Date();
		model.addAttribute("statistic", statisticService.getFibonacciStatistic());
		model.addAttribute("time", currentTime.toString());
		return "statistic";
	}
}
