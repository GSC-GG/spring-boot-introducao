package br.ifsp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Page<Address> findByContactId(Long contactId, Pageable pageable);
}
