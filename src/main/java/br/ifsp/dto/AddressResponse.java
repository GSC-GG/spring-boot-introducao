package br.ifsp.dto;

public record AddressResponse(Long id, String rua, String cidade, String estado, String cep, Long contactId) {}