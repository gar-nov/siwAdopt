package it.uniroma3.siw.service;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Categoria;
import it.uniroma3.siw.repository.CategoriaRepository;



@Service
public class CategoriaService {

	//gestione visualizzazione categoriE
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public boolean alreadyExists(Categoria categoria) {
	    return categoriaRepository.existsByNome(categoria.getNome());
	}

	
	public List<Categoria> findAll() {
		List<Categoria> categorie = new ArrayList<Categoria>();

		for (Categoria c : categoriaRepository.findAll()) {
			categorie.add(c);
		}

		return categorie;
	}
	
	//gestione salvataggio nuova categoria
	
	@Transactional
	public Categoria save(Categoria categoria) {
		return categoriaRepository.save(categoria); // dopo aver salvato, ritorna la categoria salvata
	}
	
	//gestione visualizzazione categoriA
	
	public Categoria findById(Long id) {
		return categoriaRepository.findById(id).get();
	}
}
