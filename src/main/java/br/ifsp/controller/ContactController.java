package br.ifsp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import br.ifsp.config.MapperConfig;
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
    public Page<ContactResponse> getAllContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Contact> contacts;
        contacts = contactRepository.findAll(pageable);
        return contacts.map(contact -> new MapperConfig().modelMapper().map(contact, ContactResponse.class));
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
    public Page<ContactResponse> searchContactsByName(@RequestParam String name, Pageable pageable) {
        return contactRepository.findByNomeContainingIgnoreCase(name, pageable)
                .map(contact -> new MapperConfig().modelMapper().map(contact, ContactResponse.class));
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
