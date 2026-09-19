package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

public class ChangeUserPasswordUseCase {

    private final UserRepository userRepository;

    public ChangeUserPasswordUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId, String newPassword) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.changePassword(newPassword);

        userRepository.save(user);
    }
}
