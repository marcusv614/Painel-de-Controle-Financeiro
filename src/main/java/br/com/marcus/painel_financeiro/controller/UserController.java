package br.com.marcus.painel_financeiro.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.marcus.painel_financeiro.dto.UserRequestDTO;
import br.com.marcus.painel_financeiro.dto.UserResponseDTO;
import br.com.marcus.painel_financeiro.service.UserService;

@RestController
@RequestMapping("/v1/users")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UUID> createUser(@RequestBody UserRequestDTO userDTO) {
        var userID = service.createUser(userDTO);
        return ResponseEntity.created(URI.create("/v1/users/" + userID.toString())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        UserResponseDTO dto = service.showUser(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(){
        List<UserResponseDTO> list = service.showAllUsers();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUsers(@PathVariable UUID id, @RequestBody UserRequestDTO dto) {
        UserResponseDTO responsedto = service.updateUser(id, dto);
        return ResponseEntity.ok().body(responsedto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsers(@PathVariable UUID id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
