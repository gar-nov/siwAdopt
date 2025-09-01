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

    @Transactional
    public boolean alreadyExists(Animale animale) {
        return this.animaleRepository.existsByNomeAndUserAndCategoria(
            animale.getNome(), animale.getUser(), animale.getCategoria());
    }

    public void deleteById(Long id) {
    	this.animaleRepository.deleteById(id);
    }

    


}
