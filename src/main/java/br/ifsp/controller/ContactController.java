package br.ifsp.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.dto.AddressResponse;
import br.ifsp.dto.ContactResponse;
import br.ifsp.exception.ResourceNotFoundException;
import br.ifsp.model.Contact;
import br.ifsp.repository.ContactRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public List<ContactResponse> findAll() {

        // A resposta é manipulada para evitar serialização dos dados (contato e endereços se referencinaod infinitamente na resposta)
        // Vide dto/AddressResponse.java e ContactResponse.java
        return contactRepository.findAll().stream()
                .map(contact -> new ContactResponse(
                    contact.getId(),
                    contact.getNome(),
                    contact.getTelefone(),
                    contact.getEmail(),
                    contact.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public ContactResponse getContactById(@PathVariable Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));

        return new ContactResponse(
                    contact.getId(),
                    contact.getNome(),
                    contact.getTelefone(),
                    contact.getEmail(),
                    contact.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
                );
    }

    @GetMapping("/search")
    public List<ContactResponse> getContactsByName(@RequestParam String name) {

        // Uso de filter() da Stream API para filtrar os contatos por nome
        return contactRepository.findAll()
                .stream()
                .filter(contact -> contact.getNome().contains(name))
                .map(contact -> new ContactResponse(
                    contact.getId(),
                    contact.getNome(),
                    contact.getTelefone(),
                    contact.getEmail(),
                    contact.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
                ))
                .toList();
    }

    // POST com validação dos dados (vide ValidationErrorHandler.java)
    @PostMapping
    public ContactResponse create(@RequestBody @Valid Contact contact) {
        Contact saved = contactRepository.save(contact);
        return new ContactResponse(
                    saved.getId(),
                    saved.getNome(),
                    saved.getTelefone(),
                    saved.getEmail(),
                    saved.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
                );
    }

    @GetMapping("/{id}/addresses")
    public List<AddressResponse> getAddressesByContactId(@PathVariable Long id) {

        // O contato do id passado é pego e então é retornada a lista de endereços dele
        try {
            return contactRepository.findById(id)
                    .get()
                    .getAddresses()
                    .stream()
                    .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList();
        } catch (NoSuchElementException e) {
            throw new NullPointerException("Contato não encontrado: " + id);
        }
    }

    @PutMapping("/{id}")
    public ContactResponse updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));

        existingContact.setNome(updatedContact.getNome());
        existingContact.setTelefone(updatedContact.getTelefone());
        existingContact.setEmail(updatedContact.getEmail());

        Contact saved = contactRepository.save(existingContact);
        return new ContactResponse(
            saved.getId(),
            saved.getNome(),
            saved.getTelefone(),
            saved.getEmail(),
            saved.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
        );
    }

    @PatchMapping("/{id}")
    public ContactResponse patchContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        // caso o contato não exista, um erro ocorre (veja NullPointerExceptionHandler.java)
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato não encontrado: " + id));

        // apenas os valores de campos não nulos são atribuídos ao contato
        if (updatedContact.getNome() != null) {
            existingContact.setNome(updatedContact.getNome());
        }
        if (updatedContact.getTelefone() != null) {
            existingContact.setTelefone(updatedContact.getTelefone());
        }
        if (updatedContact.getEmail() != null) {
            existingContact.setEmail(updatedContact.getEmail());
        }

        Contact saved = contactRepository.save(existingContact);
        return new ContactResponse(
            saved.getId(),
            saved.getNome(),
            saved.getTelefone(),
            saved.getEmail(),
            saved.getAddresses().stream()
                            .map(address -> new AddressResponse(
                                address.getId(),
                                address.getRua(),
                                address.getCidade(),
                                address.getEstado(),
                                address.getCep(),
                                address.getContactId()
                            ))
                            .toList()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }
}
