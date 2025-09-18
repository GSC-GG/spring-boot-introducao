package br.ifsp.controller;

import java.util.List;

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

import br.ifsp.model.Contact;
import br.ifsp.repository.ContactRepository;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }

    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));
    }

    @GetMapping("/search")
    // O nome a ser procurado é pego dos parâmetros de URL em search
    public List<Contact> getContactsByName(@RequestParam String name) {

        // Uso de filter() da Stream API para filtrar os contatos por nome
        return contactRepository.findAll()
                .stream()
                .filter(contact -> contact.getNome().contains(name))
                .toList();
    }

    @PostMapping
    public Contact create(@RequestBody Contact contact) {
        return contactRepository.save(contact);
    }

    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contato não encontrado: " + id));

        existingContact.setNome(updatedContact.getNome());
        existingContact.setTelefone(updatedContact.getTelefone());
        existingContact.setEmail(updatedContact.getEmail());

        return contactRepository.save(existingContact);
    }

    @PatchMapping("/{id}")
    public Contact patchContact(@PathVariable Long id, @RequestBody Contact updatedContact) {
        // caso o contato não exista, um erro ocorre (veja NullPointerExceptionHandler.java)
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Contato não encontrado: " + id));

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

        return contactRepository.save(existingContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        contactRepository.deleteById(id);
    }
}
