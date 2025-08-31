package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.service.CategoriaService;



@Component
public class CategoriaValidator implements Validator {

	@Autowired
	CategoriaService categoriaService;
	
	@Override
	public boolean supports(Class<?> categoriaClass) {
		return Categoria.class.equals(categoriaClass);
	}

	@Override
	public void validate(Object target, Errors errors) {
	    Categoria categoria = (Categoria) target;

	    // Nome non vuoto
	    if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
	        errors.rejectValue("nome", "required", "Il nome non può essere vuoto");
	    }

	    // Nome duplicato
	    if (this.categoriaService.alreadyExists(categoria)) {
	        errors.rejectValue("nome", "duplicate", "Categoria già esistente");
	    }
	}

}
