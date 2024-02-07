package giuliasilvestrini.geekhub.repositories;


import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SubsectionDAO extends JpaRepository <Subsection,Long>{

        Page<Subsection> findAllBySection(Section section, Pageable pageable);


}