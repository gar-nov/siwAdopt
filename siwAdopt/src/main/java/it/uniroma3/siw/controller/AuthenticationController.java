package it.uniroma3.siw.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.CredentialsValidator;
import it.uniroma3.siw.validator.UserValidator;


@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private CredentialsValidator credentialsValidator;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // Mostra il form di registrazione
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "formRegisterUser.html";
    }

    
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               @ModelAttribute("user") User user,
                               BindingResult userBindingResult,
                               Model model) {

        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "formLogin";
        }

        return "formRegisterUser.html";
    }


    // Mostra il form di login
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "formLogin";
    }

    

    // Logout manuale (in casi eccezionali)
    @GetMapping("/logout")
    public String logout(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        model.addAttribute("nome", credentials.getUser().getNome());

        // Invalida l’autenticazione manualmente
        SecurityContextHolder.getContext().setAuthentication(null);

        return "goodbye";
    }

    // Redirect dopo login con successo
    @GetMapping("/default")
    public String defaultAfterLogin(Model model) {
        return "redirect:/home";
    }

    @GetMapping("/success")
    public String getTheRightPath(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Controlla se l'utente è autenticato
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/login";  // Se non è autenticato, reindirizza alla pagina di login
        }

        // Se l'utente è autenticato, continua con il resto del codice
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roles", authentication.getAuthorities());

        // Se è admin, reindirizza alla pagina dell'admin
        if (Credentials.ADMIN_ROLE.equals(credentials.getRole())) {
            return "admin/indexAdmin.html";
        }

        // Se non è admin, reindirizza alla homepage principale
        return "user/indexUser.html";
    }

   

}
