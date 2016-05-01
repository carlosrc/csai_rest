package es.udc.csai.rest.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchRestController {

	@RequestMapping("/index")
	public String index() {
		return "Login exitoso.";
	}

}
