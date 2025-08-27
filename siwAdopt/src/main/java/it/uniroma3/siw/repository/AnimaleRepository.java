package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Animale;
import it.uniroma3.siw.model.User;



public interface AnimaleRepository extends CrudRepository<Animale,Long> {

	public List<Animale> findAllByOrderByIdDesc();
	
	List<Animale> findByUser(User user);


}
