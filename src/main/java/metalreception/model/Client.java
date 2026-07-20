package main.java.metalreception.model;

import main.java.metalreception.exception.validation.InvalidIdException;
import main.java.metalreception.exception.validation.InvalidNameException;
import main.java.metalreception.exception.validation.InvalidPhoneException;

public class Client {
    private final int id;
    private String name;
    private String phone;

    public Client(int id, String name, String phone) {
        if (id <= 0) {
            throw new InvalidIdException("ID клиента должен быть больше нуля.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Имя клиента не может быть пустым");
        }
        if (phone != null && phone.isBlank()) {
            throw new InvalidPhoneException("Телефон, если указан, не может быть пустой строкой.");
        }
        this.id = id;
        this.name = name.strip();
        this.phone = phone == null ? null : phone.strip();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Имя не может быть null.");
        }
        this.name = name.strip();
    }
    public void setPhone(String phone) {
        if (phone != null && phone.isBlank()) {
            throw new InvalidPhoneException("Телефон, если указан, не может быть пустой строкой.");
        }
        this.phone = phone == null ? null : phone.strip();
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
