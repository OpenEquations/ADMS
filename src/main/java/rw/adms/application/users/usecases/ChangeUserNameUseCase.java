package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

public class ChangeUserNameUseCase {
    private final UserRepository userRepository;

    public ChangeUserNameUseCase(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void execute(Long userId, String firstName, String lastName){
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.changeName(firstName, lastName);
        userRepository.save(user);
    }
    }
