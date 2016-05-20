package es.udc.csai.rest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

	@RequestMapping(name = "/login", method = RequestMethod.GET)
	public String goToLogin() {
		return "login";
	}

}
