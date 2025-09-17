package br.ifsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

}
