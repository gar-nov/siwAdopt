package it.uniroma3.siw.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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

}
