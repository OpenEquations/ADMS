package rw.adms.domain.users.interfaces;

import rw.adms.domain.users.User;

import java.util.List;
import java.util.Optional;



// requirements:
// users should be unique. no two records with same id should be allowed. 

public interface UserRepository {
    // interfaces are public by default
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    boolean existsById(Long id);

    void deleteById(Long id);
}