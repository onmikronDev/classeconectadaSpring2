package com.me.classeconectada.repository;

import com.me.classeconectada.model.User;
import com.me.classeconectada.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);
    List<User> findByTipo(UserType tipo);
    List<User> findByActiveTrue();
    List<User> findByTipoAndActiveTrue(UserType tipo, Boolean active);
}
