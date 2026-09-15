package rw.adms.application.users.usecases;

import java.util.Optional;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> execute(Long userId) {
        return userRepository.findById(userId);
    }
}