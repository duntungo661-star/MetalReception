package metalreception.service;

import metalreception.exception.notfound.ReceptionNotFoundException;
import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.model.ReceptionChange;
import metalreception.repository.ReceptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReceptionService implements UsageChecker {

    private final ReceptionRepository receptionRepository;

    public ReceptionService(ReceptionRepository receptionRepository) {
        this.receptionRepository = receptionRepository;
    }

    public Reception createReception(Client client, Metal metal, BigDecimal weight) {
        Reception reception = new Reception(client, metal, weight, LocalDate.now());
        return receptionRepository.save(reception);
    }

    public List<Reception> getAllReceptions() {
        return receptionRepository.findAll();
    }

    public Reception getByIdOrThrow(int id) {
        return receptionRepository.findById(id)
                .orElseThrow(() -> new ReceptionNotFoundException(
                        "Приёмка с id=" + id + " не найдена."
                ));
    }

    public List<Reception> findByClientId(int clientId) {
        return receptionRepository.findByClientId(clientId);
    }

    public List<Reception> findByMetalId(int metalId) {
        return receptionRepository.findByMetalId(metalId);
    }

    public Reception correctReceptionWeight(int receptionId, BigDecimal newWeight, String reason) {
        Reception reception = getByIdOrThrow(receptionId);
        reception.correctWeight(newWeight, reason);
        return receptionRepository.save(reception);
    }

    public List<ReceptionChange> getReceptionChanges(int receptionId) {
        Reception reception = getByIdOrThrow(receptionId);
        return reception.getChanges();
    }

    @Override
    public boolean isClientInUse(int clientId) {
        return receptionRepository.existsByClientId(clientId);
    }

    @Override
    public boolean isMetalInUse(int metalId) {
        return receptionRepository.existsByMetalId(metalId);
    }
}