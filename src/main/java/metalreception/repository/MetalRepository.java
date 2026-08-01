package metalreception.repository;

import metalreception.model.Metal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetalRepository extends JpaRepository<Metal, Integer> {

}
