package es.udc.csai.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class PublicRestController {

	@RequestMapping(name = "/index", method = RequestMethod.GET)
	public ResponseEntity<String> index() {
		return new ResponseEntity<>("página pública", HttpStatus.OK);
	}

}
