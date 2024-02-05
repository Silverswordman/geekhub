package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Location.Province;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConventionDAO extends JpaRepository<Convention, UUID> {
}
