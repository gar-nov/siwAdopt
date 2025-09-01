package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AnimaleService;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.AnimaleValidator;
import jakarta.validation.Valid;
import it.uniroma3.siw.model.Animale;


@Controller
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
    private AnimaleValidator animaleValidator;
	
	
	
	    

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


   //gestione nuovo animale
    
    @GetMapping("/animale/form")
	public String getAnimaleForm(Model model) {
		model.addAttribute("animale", new Animale());
		System.out.println("✅ Stai passando 'categorie'? ");
		model.addAttribute("categorie", this.categoriaService.findAll());
		return "animaleForm.html";
	}

    @PostMapping("/animale")
    public String addAnimale(@ModelAttribute("animale") Animale animale,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("image") MultipartFile multipartFile) throws IOException {

        // 1. Recupero l'utente corrente
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User currentUser = credentials.getUser();
        animale.setUser(currentUser); // ✅ settato prima della validazione

        // 2. Foto
        String fileName = null;
        if (!multipartFile.isEmpty()) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fileName = fileName.replaceAll("\\s+", "");
            animale.setFoto(fileName);
        }

        // 3. Validazione manuale
        animaleValidator.validate(animale, bindingResult);

        if (bindingResult.hasErrors()) {
            System.out.println("❌ Errori:");
            bindingResult.getAllErrors().forEach(e -> System.out.println(" -> " + e));
            model.addAttribute("categorie", this.categoriaService.findAll());
            return "animaleForm.html";
        }

        // 4. Salvataggio
        Animale animaleSalvato = animaleService.save(animale);
        if (!multipartFile.isEmpty()) {
            String uploadDir1 = "src/main/resources/static/images/animali-foto/" + animaleSalvato.getId();
            String uploadDir2 = "target/classes/static/images/animali-foto/" + animaleSalvato.getId();
            FileUploadUtil.saveFile(uploadDir1, fileName, multipartFile);
            FileUploadUtil.saveFile(uploadDir2, fileName, multipartFile);
        }

        return "redirect:/animale/" + animaleSalvato.getId();
    }
// gestione eliminazione di un animale
    
    @GetMapping("/animale/delete/confirm/{id}")
    public String confirmDeleteAnimale(@PathVariable("id") Long id, Model model) {

        Animale animale = this.animaleService.findById(id);

        // Utente autenticato
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User currentUser = credentials.getUser();

        // Verifica autorizzazione
        if (credentials.getRole().equals(Credentials.ADMIN_ROLE) || animale.getUser().equals(currentUser)) {
            this.animaleService.deleteById(id);
            return "redirect:/animali";
        }

        //  NON autorizzato
        return "unauthorized";
    }
    
    //gestione modifica animale
 

    @GetMapping("/animale/edit/form/{id}")
    public String editAnimaleForm(@PathVariable Long id, Model model) {
        Animale animale = animaleService.findById(id);
        model.addAttribute("animale", animale);
        model.addAttribute("categorie", this.categoriaService.findAll());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        User currentUser = credentials.getUser();

        // puoi modificare se sei ADMIN oppure proprietario dell'annuncio
        if (Credentials.ADMIN_ROLE.equals(credentials.getRole()) || animale.getUser().equals(currentUser)) {
            return "animaleEditForm";
        }
        return "unauthorized";
    }
    @PostMapping("/animale/edit/{id}")
    public String editAnimale(@ModelAttribute("animale") Animale animale,
                              BindingResult bindingResult,
                              @PathVariable Long id,
                              Model model,
                              @RequestParam("image") MultipartFile multipartFile) throws IOException {

        // Recupera l'animale originale dal DB
        Animale vecchioAnimale = this.animaleService.findById(id);

        // Mantieni ID e user esistenti
        animale.setId(vecchioAnimale.getId());
        animale.setUser(vecchioAnimale.getUser());

        // Gestione immagine PRIMA del confronto
        if (multipartFile.isEmpty()) {
            animale.setFoto(vecchioAnimale.getFoto());
        } else {
            String fileName = org.springframework.util.StringUtils.cleanPath(multipartFile.getOriginalFilename());
            fileName = fileName.replaceAll("\\s+", "");
            animale.setFoto(fileName);
        }

        // Confronto con animale vecchio
        if (!vecchioAnimale.equals(animale)) {
            this.animaleValidator.validate(animale, bindingResult);
        }

        // In caso di errori, torna al form con categorie
        if (bindingResult.hasErrors()) {
            System.out.println("❌ Errori di validazione:");
            bindingResult.getAllErrors().forEach(e -> System.out.println(" -> " + e));
            model.addAttribute("categorie", this.categoriaService.findAll());
            return "animaleEditForm";
        }

        // Salva nel database
        Animale animaleSalvato = this.animaleService.save(animale);

     // Salva immagine sul filesystem solo se è stata caricata
        if (!multipartFile.isEmpty()) {
            String uploadDir1 = "src/main/resources/static/images/animali-foto/" + animaleSalvato.getId();
            String uploadDir2 = "target/classes/static/images/animali-foto/" + animaleSalvato.getId();

            FileUploadUtil.saveFile(uploadDir1, animale.getFoto(), multipartFile);
            FileUploadUtil.saveFile(uploadDir2, animale.getFoto(), multipartFile);
        }


        // Redirect alla pagina di dettaglio
        return "redirect:/animale/" + animaleSalvato.getId();
    }






    
    






}