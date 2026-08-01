package metalreception.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPhoneException;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String phone;

    protected Client() {
        // Пустой конструктор нужен Hibernate — не использовать напрямую в коде.
    }

    public Client(String name, String phone) {
        validateName(name);
        validatePhone(phone);

        this.name = name.strip();
        this.phone = normalizePhone(phone);
    }

    public Integer getId() {
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