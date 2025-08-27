package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import it.uniroma3.siw.service.AnimaleService;

@Controller
public class AnimaleController {

    @Autowired
    private AnimaleService animaleService;

    @GetMapping("/animali")
    public String getListaAnimali(Model model) {
        model.addAttribute("animali", animaleService.findAll());
        return "animali.html";
    }
}