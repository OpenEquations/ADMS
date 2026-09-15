package rw.adms.application.users.usecases;

import rw.adms.domain.users.interfaces.UserRepository;

public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        userRepository.deleteById(userId);
    }
}