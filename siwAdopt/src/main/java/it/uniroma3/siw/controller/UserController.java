package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	
	
	  @GetMapping("/user/indexU")
	    public String indexUser(Model model) {
	       
	        return "user/indexUser.html";
	    }


}
