package metalreception.model;

import metalreception.exception.validation.InvalidIdException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPhoneException;

public class Client {

    private final int id;
    private String name;
    private String phone;

    public Client(int id, String name, String phone) {
        validateId(id);
        validateName(name);
        validatePhone(phone);

        this.id = id;
        this.name = name.strip();
        this.phone = normalizePhone(phone);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name.strip();
    }

    public void setPhone(String phone) {
        validatePhone(phone);
        this.phone = normalizePhone(phone);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new InvalidIdException(
                    "ID клиента должен быть больше нуля."
            );
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException(
                    "Имя клиента не может быть пустым."
            );
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && phone.isBlank()) {
            throw new InvalidPhoneException(
                    "Телефон, если указан, не может быть пустой строкой."
            );
        }
    }

    private String normalizePhone(String phone) {
        return phone == null ? null : phone.strip();
    }

    @Override
    public String toString() {
        String phoneText = phone == null ? "не указан" : phone;

        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phoneText + '\'' +
                '}';
    }
}