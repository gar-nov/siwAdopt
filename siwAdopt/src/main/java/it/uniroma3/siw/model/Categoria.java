package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Categoria {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @NotBlank        
	    private String nome;

	    @NotBlank
	    private String descrizione;

	   
	    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
	    private List<Animale> animali;


	    // GETTER & SETTER
	    public Long getId() {
	           return id; 
	    }
	    
	    public void setId(Long id) {
	    	        this.id = id; 
	    }

	    public String getNome() { 
	    	   return nome; 
	    }
	    
	    public void setNome(String nome) { 
	    	        this.nome = nome; 
	    }

	    public String getDescrizione() { 
	    	return descrizione;
	    }
	    
	    public void setDescrizione(String descrizione) { 
	    	        this.descrizione = descrizione; 
	    }

	    public List<Animale> getAnimali() { 
	    	   return animali; 
	    }
	    public void setAnimali(List<Animale> animali) { 
	    	        this.animali = animali; 
	    }

	    // equals & hashCode 
	    @Override
	    public int hashCode() { 
	    	       return Objects.hash(nome, descrizione); }
        
	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;
	        Categoria other = (Categoria) obj;
	        return Objects.equals(nome, other.nome)
	                && Objects.equals(descrizione, other.descrizione);
	    }
}
