package rw.adms.application.users.usecases;

import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.UserRepository;

public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(
            String firstName,
            String lastName,
            String email,
            String password
    ) {

        User user = new User(
                firstName,
                lastName,
                email,
                password
        );

        userRepository.save(user);
    }
}