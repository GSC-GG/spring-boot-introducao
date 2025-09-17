package br.ifsp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Address {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String cidade;
    private String estado;
    private String cep;

    // Chave estrangeira
    @Column(name = "contact_id", insertable = false, updatable = false)
    private Long contactId;

    @ManyToOne
    @JoinColumn(name="contact_id", nullable=false)
    private Contact contact;

    public Address() {}

    public Address(String cep, String cidade, Long contactId, String estado, Long id, String rua) {
        this.cep = cep;
        this.cidade = cidade;
        this.contactId = contactId;
        this.estado = estado;
        this.id = id;
        this.rua = rua;
    }

    public Long getId() {
        return id;
    }

    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }
    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getContactId() {
        return contactId;
    }
    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Contact getContact() {
        return contact;
    }
    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
