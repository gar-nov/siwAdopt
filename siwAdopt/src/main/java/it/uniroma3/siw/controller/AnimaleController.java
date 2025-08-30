package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AnimaleService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.model.Animale;


@Controller
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    
	@Autowired
	private CredentialsService credentialsService;
	
	
	
	    

    @GetMapping("/animali")
    public String getListaAnimali(Model model) {
        model.addAttribute("animali", animaleService.findAll());
        return "animali.html";
    }
    
    
    @GetMapping("/user/my-animals")
    public String getMyAnimals(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User currentUser = credentials.getUser();

        List<Animale> animali = animaleService.findByUser(currentUser);
        model.addAttribute("animali", animali);
        return "user/myAnimals.html"; // nome a tua scelta
    }

    @GetMapping("/animale/{id}")
    public String getAnimaleDetails(@PathVariable("id") Long id, Model model) {
        Animale animale = animaleService.findById(id);
        model.addAttribute("animale", animale);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
            model.addAttribute("currentUserId", credentials.getUser().getId());
        }

        return "animaleDetail"; // carica src/main/resources/templates/animaleDetail.html
    }


   




}