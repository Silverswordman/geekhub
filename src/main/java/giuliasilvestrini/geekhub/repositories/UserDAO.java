package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Location.Province;
import giuliasilvestrini.geekhub.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDAO extends JpaRepository<User, UUID> {
}
