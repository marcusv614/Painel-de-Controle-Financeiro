package br.com.marcus.painel_financeiro.dto;

import br.com.marcus.painel_financeiro.model.User;

public record UserResponseDTO(String username, String password, String email) {

    public UserResponseDTO(User model) {
        this(
            model.getUsername(),
            model.getPassword(),
            model.getEmail()
        );
    }

}