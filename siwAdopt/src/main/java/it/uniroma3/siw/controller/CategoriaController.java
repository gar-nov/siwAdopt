package it.uniroma3.siw.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;



import it.uniroma3.siw.model.Animale;
import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.service.CategoriaService;
import it.uniroma3.siw.validator.CategoriaValidator;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;


@Controller
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private CategoriaValidator categoriaValidator;
	
	

	
	@GetMapping("/categoried")
	public String getCategorie(Model model) {
	    System.out.println("✅ Metodo getCategorie() eseguito");

	    List<Categoria> categorie = categoriaService.findAll();
	    model.addAttribute("categorie", categorie);
	   

	    return "categorie";
	}
	@GetMapping("/admin/categoria/form")
	public String showCategoriaForm(Model model) {
	    model.addAttribute("categoria", new Categoria());
	    model.addAttribute("categorie", categoriaService.findAll());
	    return "admin/categoriaForm";
	}

	@PostMapping("/admin/categoria")
	public String addCategoria(@Valid @ModelAttribute("categoria") Categoria categoria,
	                           BindingResult bindingResult,
	                           Model model) {

	    // Validazione personalizzata
	    categoriaValidator.validate(categoria, bindingResult);

	    if (!bindingResult.hasErrors()) {
	        categoriaService.save(categoria);
	        // redirect alla lista aggiornata
	        return "redirect:/categoried";
	    }

	    // Se ci sono errori, torno alla lista con il form
	    model.addAttribute("categorie", categoriaService.findAll());
	    return "admin/categoriaForm";
	}
	
	@GetMapping("/categoria/{id}")
	public String getCategoria(@PathVariable("id") Long id, Model model) {
		Categoria categoria = categoriaService.findById(id);
		model.addAttribute("categoria", categoria);
		
		List<Animale> animali = categoria.getAnimali();
		
		Collections.reverse(animali); //così appaiono dal più recente al meno recente
		
		model.addAttribute("animali", animali);
		
		return "categoria";
	}
	
	
	@PostConstruct
	public void testLoaded() {
	    System.out.println("✅ CategoriaController CARICATO!");
	}
	@GetMapping("/admin/test-categorie")
	@ResponseBody
	public String testCategorieRoute() {
	    return "Funziona!";
	}
	@GetMapping("/test-html")
	public String testHtml() {
	    return "test";
	}

}
