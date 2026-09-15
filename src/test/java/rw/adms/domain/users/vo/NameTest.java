package rw.adms.domain.users.vo;

import org.junit.jupiter.api.Test;
import rw.adms.domain.users.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NameTest {
    @Test
    void shouldCreateUser() {

        Name name = new Name(
                "Bonheur",
                "Iradukunda"
        );

        assertEquals("Bonheur", name.getFirstName());
        assertEquals("Iradukunda", name.getLastName());
    }

}
