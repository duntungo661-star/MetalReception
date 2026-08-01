package metalreception.model;

import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPhoneException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void shouldCreateClientWithValidData() {
        Client client = new Client("Иван Иванов", "+79991234567");

        assertEquals("Иван Иванов", client.getName());
        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldAllowNullPhone() {
        Client client = new Client("Иван", null);

        assertNull(client.getPhone());
    }

    @Test
    void shouldStripWhitespaceFromNameAndPhone() {
        Client client = new Client("  Иван  ", "  +79991234567  ");

        assertEquals("Иван", client.getName());
        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(InvalidNameException.class,
                () -> new Client(null, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> new Client("   ", null));
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsBlankButNotNull() {
        assertThrows(InvalidPhoneException.class,
                () -> new Client("Иван", "   "));
    }

    @Test
    void shouldUpdateNameThroughSetter() {
        Client client = new Client("Иван", null);

        client.setName("Пётр");

        assertEquals("Пётр", client.getName());
    }

    @Test
    void setNameShouldThrowExceptionWhenBlank() {
        Client client = new Client("Иван", null);

        assertThrows(InvalidNameException.class,
                () -> client.setName(""));
    }

    @Test
    void shouldUpdatePhoneThroughSetter() {
        Client client = new Client("Иван", null);

        client.setPhone("+79991234567");

        assertEquals("+79991234567", client.getPhone());
    }

    @Test
    void shouldClearPhoneWhenSetToNull() {
        Client client = new Client("Иван", "+79991234567");

        client.setPhone(null);

        assertNull(client.getPhone());
    }

    @Test
    void setPhoneShouldThrowExceptionWhenBlankButNotNull() {
        Client client = new Client("Иван", null);

        assertThrows(InvalidPhoneException.class,
                () -> client.setPhone("   "));
    }
}