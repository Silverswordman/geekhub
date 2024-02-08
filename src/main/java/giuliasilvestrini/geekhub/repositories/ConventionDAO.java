package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Convention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ConventionDAO extends JpaRepository<Convention, UUID> {
    Optional<Convention> findByTitle(String title);

}
