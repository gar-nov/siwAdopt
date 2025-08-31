package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import it.uniroma3.siw.model.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria,Long> {
	public boolean  existsByNome(String nome);
	public List<Categoria> findByOrderByNome();

}
