package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;

public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public AuthenticateUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    public Optional<User> execute(String email, String password) {

        return userRepository.findByEmail(email)
                .filter(user -> passwordHasher.matches(password, user.getPassword()));
    }
}
