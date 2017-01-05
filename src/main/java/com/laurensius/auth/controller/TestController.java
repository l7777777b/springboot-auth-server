package com.laurensius.auth.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laurensius.auth.domain.User;
import com.laurensius.auth.repository.UserRepository;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	UserRepository userRepository;
	
	@RequestMapping("")
	public String test() {
		return "THIS IS AUTH SERVER TEST";
	}
	
	@RequestMapping("/principal")
	public String testprincipal(Principal principal) {
		return "You're authenticated on auth server: "+principal.getName();
	}
	
	@RequestMapping("/me")
	public String testme(Principal principal) {
		User u = userRepository.findOneByEmail(principal.getName());
		return "You're authenticated on auth server: "+u.getEmail();
	}
	
	
}
