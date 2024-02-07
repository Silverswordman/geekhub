package giuliasilvestrini.geekhub.repositories;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SectionDAO extends JpaRepository<Section,Long> {
    Page<Section> findAllByConvention(Convention convention, Pageable pageable);
    Optional<Section> findBySectionTitle(String sectionTitle);

}


