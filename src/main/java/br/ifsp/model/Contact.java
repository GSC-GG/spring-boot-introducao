package br.ifsp.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    // Anotações para restrição de valores aos atributos e mensagens de erro caso a regra de validação não seja obedecida
    @NotNull(message="Nome não pode ser nulo")
    private String nome;
    @Size(min=8, max=15, message="O telefone deve ter entre 8 e 15 caracteres")
    private String telefone;
    @Email(message="E-mail inválido")
    private String email;

    // Anotação para a relação de 1 contato com vários endereços subordinados
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @NotEmpty(message = "O contato deve ter pelo menos um endereço")
    private List<Address> addresses;


    public Contact() {this.addresses = new ArrayList<>();
}

    public Contact(String nome, String telefone, String email) {
        this.addresses = new ArrayList<>();
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddresses() {
        return addresses;
    }
    public void setAddresses(List<Address> addresses) {
    if (addresses != null) {
        addresses.forEach(address -> address.setContact(this)); 
        
        if (this.addresses == null) { 
            this.addresses = new ArrayList<>();
        }
        
        this.addresses.clear(); 
        this.addresses.addAll(addresses);         
    }
}


    public void addAddress(Address address) {
        addresses.add(address);
        address.setContact(this);
    }
    public void removeAddress(Address address) {
        addresses.remove(address);
        address.setContact(null);
    }
}