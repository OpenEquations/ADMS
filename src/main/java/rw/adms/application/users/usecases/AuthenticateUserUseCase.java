package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.Optional;

public class AuthenticateUserUseCase {

    private final UserRepository userRepository;

    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(String email, String password) {

        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }
}
