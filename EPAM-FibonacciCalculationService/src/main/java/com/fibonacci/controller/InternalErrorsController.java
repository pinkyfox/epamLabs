package com.fibonacci.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@Controller
public class InternalErrorsController implements ErrorController {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@RequestMapping("/error")
	public String handleResourceNotFoundException(HttpServletRequest request, ModelMap modelMap) {
		logger.error("Internal error occurred");
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		modelMap.put("message", exception.getLocalizedMessage());
		return "errors";
	}

}
