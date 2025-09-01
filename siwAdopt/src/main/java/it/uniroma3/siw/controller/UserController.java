package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private CredentialsService credentialsService;
	

	@Autowired
	private UserService userService;
	
	@GetMapping("/user/all")
	public String getUsers(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);

		return "users.html";
	}
	
	@GetMapping("/user/{id}")
	public String getUser(@PathVariable("id") Long id, Model model) {
	    User user = userService.getUser(id);
	    model.addAttribute("user", user);

	    // Aggiungi anche currentUserId per mostrare bottoni solo al proprietario
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    User currentUser = credentials.getUser();
	    model.addAttribute("currentUserId", currentUser.getId());

	    return "user.html";
	}

	@GetMapping("/user/profile")
	public String redirectToMyProfile() {
	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	    Long currentUserId = credentials.getUser().getId();

	    return "redirect:/user/" + currentUserId;
	}



}
