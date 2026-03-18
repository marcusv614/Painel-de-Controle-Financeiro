package br.com.marcus.painel_financeiro.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.marcus.painel_financeiro.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
