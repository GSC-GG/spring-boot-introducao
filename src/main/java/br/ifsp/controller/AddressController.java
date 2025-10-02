package br.ifsp.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.config.MapperConfig;
import br.ifsp.dto.AddressResponse;
import br.ifsp.exception.ResourceNotFoundException;
import br.ifsp.model.Address;
import br.ifsp.model.Contact;
import br.ifsp.repository.AddressRepository;
import br.ifsp.repository.ContactRepository;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping
    public List<AddressResponse> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(address -> new AddressResponse(
                    address.getId(),
                    address.getRua(),
                    address.getCidade(),
                    address.getEstado(),
                    address.getCep(),
                    address.getContactId()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public AddressResponse getAddressById(@PathVariable Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado: " + id));
        return new AddressResponse(
                address.getId(),
                address.getRua(),
                address.getCidade(),
                address.getEstado(),
                address.getCep(),
                address.getContactId()
        );
    }
    
    @GetMapping("/contacts/{contactId}")
    public Page<AddressResponse> getAddressesByContact(@PathVariable Long contactId, Pageable pageable) {
        return addressRepository.findByContactId(contactId, pageable)
                .map(address -> {
            return new MapperConfig().modelMapper().map(address, AddressResponse.class);
        });
    }

    @PostMapping
    public AddressResponse createAddress(@RequestBody Address address) {
        Contact contact = contactRepository.findById(address.getContactId()).get();
        address.setContact(contact);
        Address saved = addressRepository.save(address);
        return new AddressResponse(
                saved.getId(),
                saved.getRua(),
                saved.getCidade(),
                saved.getEstado(),
                saved.getCep(),
                saved.getContactId()
        );
    }
}
