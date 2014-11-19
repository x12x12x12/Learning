package com.server.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		model.addAttribute("location","index");
		return "index";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String test(Model model) {
		model.addAttribute("location","test");
		return "game";
	}
	
//	@MessageMapping("/chat")
//	@SendTo("/data/contents")
//	public ChatDataType chat (ChatDataType chatDataType) throws Exception{
//		
//		return chatDataType;
//	}
//	
	
}
