package main.java.metalreception.service;

import main.java.metalreception.exception.notfound.ReceptionNotFoundException;
import main.java.metalreception.model.Client;
import main.java.metalreception.model.Metal;
import main.java.metalreception.model.Reception;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReceptionService implements UsageChecker {
    private final List<Reception> receptions = new ArrayList<>();
    private int nextId = 1;

    public Reception createReception(Client client, Metal metal, BigDecimal weight) {
        Reception reception = new Reception(
                nextId, client, metal, weight, LocalDate.now()
        );
        receptions.add(reception);
        nextId++;
        return reception;
    }

    public List<Reception> getAllReceptions() {
        return new ArrayList<>(receptions);
    }

    public Optional<Reception> findById(int id) {
        for (Reception reception : receptions) {
            if (reception.getId() == id) {
                return Optional.of(reception);
            }
        }
        return Optional.empty();
    }

    public Reception getByIdOrThrow(int id) {
        return findById(id).orElseThrow(() -> new ReceptionNotFoundException("Приёмка с id=" + id + " не найдена."));
    }

    public List<Reception> findByClientId(int clientId) {
        List<Reception> result = new ArrayList<>();
        for (Reception reception : receptions) {
            if (reception.getClient().getId() == clientId) {
                result.add(reception);
            }
        }
        return result;
    }

    public List<Reception> findByMetalId(int metalId) {
        List<Reception> result = new ArrayList<>();
        for (Reception reception : receptions) {
            if (reception.getMetal().getId() == metalId) {
                result.add(reception);
            }
        }
        return result;
    }

    public Reception updateReceptionWeight(int id, BigDecimal newWeight) {
        Reception reception = getByIdOrThrow(id);
        reception.setWeight(newWeight);
        return reception;
    }

    @Override
    public boolean isClientInUse(int clientId) {
        return !findByClientId(clientId).isEmpty();
    }

    @Override
    public boolean isMetalInUse(int metalId) {
        return !findByMetalId(metalId).isEmpty();
    }
}
