package metalreception.repository;

import metalreception.model.Reception;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceptionRepository extends JpaRepository<Reception, Integer> {

    List<Reception> findByClientId(Integer clientId);

    List<Reception> findByMetalId(Integer metalId);

    boolean existsByClientId(Integer clientId);

    boolean existsByMetalId(Integer metalId);
}