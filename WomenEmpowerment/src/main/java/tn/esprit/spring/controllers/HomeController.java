package tn.esprit.spring.controllers;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
    @GetMapping
    public String homeController() {
    	return "Home Page !";
    }
    
    @GetMapping("/username")
    public Principal user(Principal principal) {
    	System.out.println("username:" + principal.getName());
    	return principal;
    }

}
