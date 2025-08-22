package it.uniroma3.siw.model;



import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Animale {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String nome;
    
    @NotBlank
    private String razza;
    
    @NotNull
    private Integer eta;

    @NotBlank
    private String descrizione;
    
    @Column(nullable = true, length = 64)
    private String foto;

    //Campi di contatto
    private String telefono;
    private String email;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@NotNull
    private User user;  
    
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

    public String getRazza() {
        return razza;
    }

    public void setRazza(String razza) {
        this.razza = razza;
    }

    public Integer getEta() {
        return eta;
    }

    public void setEta(Integer eta) {
        this.eta = eta;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /** Percorso immagine come in Prodotto (adattato ad Animale). */
    public String getFotoImagePath() {
        if (this.foto == null || this.id == null)
            return null;
        return "/images/animale-foto/" + this.id + "/" + this.foto;
    }
    
    // HASHCODE & EQUALS
    @Override
    public int hashCode() {
        return Objects.hash(categoria, descrizione, foto, nome, razza, eta);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Animale other = (Animale) obj;
        return Objects.equals(this.categoria, other.categoria)
                && Objects.equals(this.descrizione, other.descrizione)
                && Objects.equals(this.foto, other.foto)
                && Objects.equals(this.nome, other.nome)
                && Objects.equals(this.razza, other.razza)
                && Objects.equals(this.eta, other.eta);
    }
}
