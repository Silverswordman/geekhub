package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionDAO extends JpaRepository<Section, UUID> {
    Page<Section> findAllByConvention(Convention convention, Pageable pageable);

}


