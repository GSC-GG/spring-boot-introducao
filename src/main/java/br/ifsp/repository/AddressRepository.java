package br.ifsp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.ifsp.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
