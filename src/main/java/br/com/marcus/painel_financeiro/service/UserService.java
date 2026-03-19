package br.com.marcus.painel_financeiro.service;

import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.marcus.painel_financeiro.dto.UserRequestDTO;
import br.com.marcus.painel_financeiro.model.User;
import br.com.marcus.painel_financeiro.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public UUID createUser(UserRequestDTO dto) {
        var entity = new User(
            null,
            dto.username(),
            dto.password(),
            dto.email(),
            Instant.now(),
            null
        );
        var userSaved = repo.save(entity);

        return userSaved.getUserid();
    }
}
