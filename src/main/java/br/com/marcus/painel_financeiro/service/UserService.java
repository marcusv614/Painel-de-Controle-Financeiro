package br.com.marcus.painel_financeiro.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.marcus.painel_financeiro.dto.UserRequestDTO;
import br.com.marcus.painel_financeiro.dto.UserResponseDTO;
import br.com.marcus.painel_financeiro.exceptions.UserNotFoundException;
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

    public UserResponseDTO showUser(UUID id) {
        User model = repo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
        UserResponseDTO response = new UserResponseDTO(model);
        return response;
    }

    public List<UserResponseDTO> showAllUsers() {
        List<UserResponseDTO> list = repo.findAll()
        .stream()
        .map((User model) -> new UserResponseDTO(model))
        .toList();

        return list;
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        User model = repo.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

        model.setUsername(dto.username());
        model.setPassword(dto.password());
        model.setEmail(dto.email());
        model.setUpdateTimeStamp(Instant.now());

        User savedUser = repo.save(model);
        UserResponseDTO savedUserDTO = new UserResponseDTO(savedUser);
        return savedUserDTO;
    }

    public void deleteUser(UUID id) {
        repo.deleteById(id);
    }
}
