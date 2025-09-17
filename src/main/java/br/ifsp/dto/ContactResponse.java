package br.ifsp.dto;

import java.util.List;

public record ContactResponse(Long id, String nome, String telefone, String email, List<AddressResponse> addresses) {}