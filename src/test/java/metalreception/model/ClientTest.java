package metalreception.model;

import metalreception.exception.validation.InvalidIdException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPhoneException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldCreateClientWithValidData() {
        Client client = new Client(1, "Иван Иванов", "+79991234567");

        assertEquals(1, client.getId());
        assertEquals("Иван Иванов", client.getName());
        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldAllowNullPhone() {
        Client client = new Client(1, "Иван", null);

        assertNull(client.getPhone());
    }

    @Test
    void shouldStripWhitespaceFromNameAndPhone() {
        Client client = new Client(1, "  Иван  ", "  +79991234567  ");

        assertEquals("Иван", client.getName());
        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldThrowExceptionWhenIdIsZeroOrNegative() {
        assertThrows(InvalidIdException.class,
                () -> new Client(0, "Иван", null));
        assertThrows(InvalidIdException.class,
                () -> new Client(-1, "Иван", null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(InvalidNameException.class,
                () -> new Client(1, null, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> new Client(1, "   ", null));
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsBlankButNotNull() {
        assertThrows(InvalidPhoneException.class,
                () -> new Client(1, "Иван", "   "));
    }

    @Test
    void shouldUpdateNameThroughSetter() {
        Client client = new Client(1, "Иван", null);

        client.setName("Пётр");

        assertEquals("Пётр", client.getName());
    }

    @Test
    void setNameShouldThrowExceptionWhenBlank() {
        Client client = new Client(1, "Иван", null);

        assertThrows(InvalidNameException.class,
                () -> client.setName(""));
    }

    @Test
    void shouldUpdatePhoneThroughSetter() {
        Client client = new Client(1, "Иван", null);

        client.setPhone("+79991234567");

        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldClearPhoneWhenSetToNull() {
        Client client = new Client(1, "Иван", "+79991234567");

        client.setPhone(null);

        assertNull(client.getPhone());
    }

    @Test
    void setPhoneShouldThrowExceptionWhenBlankButNotNull() {
        Client client = new Client(1, "Иван", null);

        assertThrows(InvalidPhoneException.class,
                () -> client.setPhone("   "));
    }
}