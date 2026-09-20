package rw.adms.infrastructure.persistence.adapters;

import org.springframework.stereotype.Repository;
import rw.adms.domain.users.User;
import rw.adms.domain.users.enums.Permission;
import rw.adms.domain.users.interfaces.UserRepository;
import rw.adms.infrastructure.persistence.entities.UserJpaEntity;
import rw.adms.infrastructure.persistence.repositories.SpringDataUserRepository;

import java.util.EnumSet;
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
                    user.getPassword(),
                    user.getRole()
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

            entity.setRole(
                    user.getRole()
            );
        }

        entity.setPermissions(
                user.getPermissions().isEmpty()
                        ? EnumSet.noneOf(Permission.class)
                        : EnumSet.copyOf(user.getPermissions())
        );

        UserJpaEntity savedEntity =
                repository.save(entity);

        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {

        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return repository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(Long id) {

        return repository.existsById(id);
    }

    @Override
    public boolean existsAny() {

        return repository.count() > 0;
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }

    private User toDomain(UserJpaEntity entity) {

        return User.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getPermissions()
        );
    }
}
