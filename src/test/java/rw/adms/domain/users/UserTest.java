package rw.adms.domain.users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void shouldCreateUser() {

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        assertEquals("Bonheur Iradukunda", user.getName());
        assertEquals("bonheur@example.com", user.getEmail());
    }


    @Test
    void shouldChangeFirstName() {

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        user.changeFirstName("Joseph");

        assertEquals("Joseph Iradukunda", user.getName());
    }

    @Test
    void shouldChangePassword() {

        User user = new User(
                "Bonheur",
                "Iradukunda",
                "bonheur@example.com",
                "password123"
        );

        user.changePassword("newPassword456");

        assertEquals("newPassword456", user.getPassword());
    }
}