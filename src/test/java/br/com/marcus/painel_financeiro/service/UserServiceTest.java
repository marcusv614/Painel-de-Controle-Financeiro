package br.com.marcus.painel_financeiro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.marcus.painel_financeiro.dto.UserRequestDTO;
import br.com.marcus.painel_financeiro.exceptions.UserNotFoundException;
import br.com.marcus.painel_financeiro.model.User;
import br.com.marcus.painel_financeiro.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    // Padrão de organização de testes (AAA): 
    // Arrange
    // Act
    // Assert

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> idUserArgumentCaptor;

    @Mock
    private UserRepository repo;

    @InjectMocks
    private UserService service;

    @Nested
    class CreateUser {
        

        @Test
        @DisplayName("Should create user with sucess")
        void shouldCreateUserWithSucess(){
            //Arrange
            var user = new User(
                UUID.randomUUID(),
                "username",
                "1234",
                "email@email.com",
                Instant.now(),
                null
                //cria um usuário para retornar no método
            );  

            //retorna o usuário quando o repo.save() é chamado
            doReturn(user).when(repo).save(userArgumentCaptor.capture());

            //input do método. Simula o RequestBody que vem na requisição
            var input = new UserRequestDTO(
                "username",
                "1234",
                "email@email.com"
                );

            //Act
            var output = service.createUser(input);

            var userCaptured = userArgumentCaptor.getValue();

            //Assert
            assertNotNull(output);
            assertEquals(input.username(), userCaptured.getUsername());
            assertEquals(input.password(), userCaptured.getPassword());
            assertEquals(input.email(), userCaptured.getEmail());
        }

        @Test
        @DisplayName("Should throw execption when error occurs")
        void shouldThrowExcetionWhenErrorOccurs() {
            //Arrange
            //Deve retornar UserNotFoundException caso o usuario nao seja encontrado
            doThrow(new UserNotFoundException()).when(repo).save(any());

            //input do método. Simula o RequestBody que vem na requisição
            var input = new UserRequestDTO(
                "username",
                "1234",
                "email@email.com"
                );

            //Act & Assert
            assertThrows(
                UserNotFoundException.class, () -> service.createUser(input));
        }
    }

    @Nested
    class ShowUser {
        @Test
        @DisplayName("Should get user by id with sucess when user is present")
        void shouldGetUserByIdWithSucessWhenUserIsPresent() {
            //Arrange 
            var user = new User(
                UUID.randomUUID(),
                "username",
                "1234",
                "email@email.com",
                Instant.now(),
                null
                //cria um usuário para retornar no método
            );
            doReturn(Optional.of(user)).when(repo).findById(idUserArgumentCaptor.capture());
            //Act
            var output = service.showUser(user.getUserid());
            //Assert
            assertNotNull(output);
            assertEquals(user.getUserid(), idUserArgumentCaptor.getValue());
            assertEquals(user.getPassword(), output.password());
            assertEquals(user.getUsername(), output.username());
            assertEquals(user.getEmail(), output.email());
        }

        @Test
        @DisplayName("Should throw execption when user not found")
        void shouldThrowExcetionWhenUserNotFound() {
            //Arrange
            var id = UUID.randomUUID();

            doReturn(Optional.empty())
                .when(repo)
                .findById(idUserArgumentCaptor.capture());
            
            //Act & Assert 
            assertThrows(UserNotFoundException.class, () -> service.showUser(id));

            assertEquals(id, idUserArgumentCaptor.getValue());
        }
    }

    @Nested
    class ShowAllUsers {

        @Test
        @DisplayName("Should return all users with sucess")
        void shouldReturnAllUsersWithSucess() {
            //Arrange
            var user = new User(
                UUID.randomUUID(),
                "username",
                "1234",
                "email@email.com",
                Instant.now(),
                null
            );
            var userList = List.of(user);
            doReturn(userList)
                .when(repo)
                .findAll();

            //Act
            var output = service.showAllUsers();

            //Assert
            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }

    @Nested
    class DeleteUser {

        @Test
        @DisplayName("Should delete user with sucess")
        void shouldDeleteUserWithSucess() {
               // Arrange
            var id = UUID.randomUUID();

            var user = new User(
                id,
                "username",
                "1234",
                "email@email.com",
                Instant.now(),
                null
            );

            doReturn(Optional.of(user))
                .when(repo)
                .findById(id);

            doNothing()
                .when(repo)
                .delete(user);

            //Act
            service.deleteUser(id);

            //Assert
            verify(repo).findById(id);
            verify(repo).delete(user);
        }

        @Test
        @DisplayName("Should throw exception when deleting non-existing user")
        void shouldThrowExceptionWhenDeletingNonExistingUser() {
            // Arrange
            var id = UUID.randomUUID();

            doReturn(Optional.empty())
                .when(repo)
                .findById(id);

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> service.deleteUser(id));
        }
    }

    @Nested
    class UpdateUser {

        @Test
        @DisplayName("Should update user with success")
        void shouldUpdateUserWithSuccess() {
            // Arrange
            var id = UUID.randomUUID();
            var existingUser = new User(
                id,
                "username",
                "1234",
                "email@email.com",
                Instant.now(),
                null
            );

            var dto = new UserRequestDTO("newuser", "newpass", "new@email.com");

            doReturn(Optional.of(existingUser)).when(repo).findById(id);
            doReturn(new User(id, dto.username(), dto.password(), dto.email(), existingUser.getCreationTimeStamp(), Instant.now()))
                .when(repo).save(existingUser);

            // Act
            var response = service.updateUser(id, dto);

            // Assert
            assertNotNull(response);
            assertEquals(dto.username(), response.username());
            assertEquals(dto.password(), response.password());
            assertEquals(dto.email(), response.email());
        }

        @Test
        @DisplayName("Should throw exception when update user not found")
        void shouldThrowExceptionWhenUpdateUserNotFound() {
            // Arrange
            var id = UUID.randomUUID();
            var dto = new UserRequestDTO("newuser", "newpass", "new@email.com");

            doReturn(Optional.empty()).when(repo).findById(id);

            // Act & Assert
            assertThrows(UserNotFoundException.class, () -> service.updateUser(id, dto));
        }
    }
}