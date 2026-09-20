package rw.adms.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import rw.adms.domain.users.User;
import rw.adms.domain.users.interfaces.PasswordHasher;
import rw.adms.domain.users.interfaces.UserRepository;

import java.util.regex.Pattern;

/**
 * One-time, self-healing upgrade for accounts created before password
 * hashing existed. Their password column holds the raw plaintext password
 * (that was the whole bug), which this runner treats as the "raw password"
 * and re-hashes in place - it never needs to know the value in advance.
 * Idempotent: a value already shaped like a bcrypt hash is left alone, so
 * this is a no-op on every run after the first.
 */
@Component
public class PasswordMigrationRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PasswordMigrationRunner.class);
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[aby]\\$\\d{2}\\$.{53}$");

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public PasswordMigrationRunner(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void run(ApplicationArguments args) {

        int migrated = 0;

        for (User user : userRepository.findAll()) {

            if (BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {
                continue;
            }

            user.changePassword(passwordHasher.hash(user.getPassword()));
            userRepository.save(user);
            migrated++;
        }

        if (migrated > 0) {
            log.info("Hashed {} legacy plaintext password(s) on startup.", migrated);
        }
    }
}
