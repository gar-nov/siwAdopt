package it.uniroma3.siw.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Animale;
import it.uniroma3.siw.service.AnimaleService;

@Component
public class AnimaleValidator implements Validator {

    @Autowired
    private AnimaleService animaleService;

    @Override
    public boolean supports(Class<?> animaleClass) {
        return Animale.class.equals(animaleClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (this.animaleService.alreadyExists((Animale)target)) {
            errors.reject("animale.duplicato");
        }
    }
}
