package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

public class ChangeUserEmailUseCase {

    private final UserRepository userRepository;

    public ChangeUserEmailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId, String newEmail) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.changeEmail(newEmail);

        userRepository.save(user);
    }
}