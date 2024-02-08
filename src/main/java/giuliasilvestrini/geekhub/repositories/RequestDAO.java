package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RequestDAO extends JpaRepository<Request,UUID> {
    Optional<Request> findById(UUID requestId);
}
