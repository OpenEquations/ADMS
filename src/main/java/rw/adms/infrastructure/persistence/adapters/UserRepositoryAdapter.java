package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;
import rw.adms.infrastructure.persistence.entities.UserJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataUserRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(
            SpringDataUserRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        UserJpaEntity entity;

        if (user.getId() == null) {

            entity = new UserJpaEntity(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPassword()
            );

        } else {

            entity = repository.findById(
                    user.getId()
            ).orElseThrow(() ->
                    new IllegalArgumentException(
                            "User not found: "
                                    + user.getId()
                    )
            );

            entity.setFirstName(
                    user.getFirstName()
            );

            entity.setLastName(
                    user.getLastName()
            );

            entity.setEmail(
                    user.getEmail()
            );

            entity.setPassword(
                    user.getPassword()
            );
        }

        UserJpaEntity savedEntity =
                repository.save(entity);

        return User.reconstitute(
                savedEntity.getId(),
                savedEntity.getFirstName(),
                savedEntity.getLastName(),
                savedEntity.getEmail(),
                savedEntity.getPassword()
        );
    }

    @Override
    public Optional<User> findById(Long id) {

        return repository.findById(id)
                .map(entity ->
                        User.reconstitute(
                                entity.getId(),
                                entity.getFirstName(),
                                entity.getLastName(),
                                entity.getEmail(),
                                entity.getPassword()
                        )
                );
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(entity ->
                        User.reconstitute(
                                entity.getId(),
                                entity.getFirstName(),
                                entity.getLastName(),
                                entity.getEmail(),
                                entity.getPassword()
                        )
                )
                .toList();
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }
}