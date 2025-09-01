package it.uniroma3.siw.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Animale;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.AnimaleRepository;

@Service
public class AnimaleService {

    @Autowired
    private AnimaleRepository animaleRepository;

    public List<Animale> findAll() {
		List<Animale> animali = animaleRepository.findAllByOrderByIdDesc(); 
		

		return animali;
	}
    
    public List<Animale> findByUser(User user) {
        return animaleRepository.findByUser(user);
    }
    
    public Animale findById(Long id) {
        return animaleRepository.findById(id).get();
    }

    @Transactional
    public Animale save(Animale animale) {
        return animaleRepository.save(animale);
    }

    

    public boolean alreadyExists(Animale animale) {
        Animale esistente = this.animaleRepository.findByNomeAndRazzaAndEtaAndCategoriaAndUser(
            animale.getNome(),
            animale.getRazza(),
            animale.getEta(),
            animale.getCategoria(),
            animale.getUser()
        );

        if (esistente == null)
            return false;

        // Se è lo stesso animale (stesso ID), non è un duplicato
        return !esistente.getId().equals(animale.getId());
    }

    public void deleteById(Long id) {
    	this.animaleRepository.deleteById(id);
    }

    


}
