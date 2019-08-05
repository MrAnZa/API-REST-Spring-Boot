package com.zamora.profesoresplatzi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainContoller {
	
	@RequestMapping("/")
	@ResponseBody
	public String index() {
		String response = "Bienvenido a Platzi";
		return response;
	}
}
