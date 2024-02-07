package giuliasilvestrini.geekhub.services;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;

import giuliasilvestrini.geekhub.payloads.SubsectionDTO;
import giuliasilvestrini.geekhub.repositories.SubsectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SubsectionService {

    @Autowired
    private SubsectionDAO subsectionDAO;

    @Autowired
    SectionService sectionService;

    public Page<Subsection> findAll(Section section, int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return subsectionDAO.findAllBySection(section, pageable);
    }

    public Subsection findById(Long subsectionId) {
        return subsectionDAO.findById(subsectionId).orElseThrow(() -> new NotFoundException(subsectionId));
    }

    public Subsection saveSubsection(SubsectionDTO subsectionDTO, String sectionTitle) {
        Section section = sectionService.findBySectionTitle(sectionTitle);
        if (section == null) {
            throw new NotFoundException("Questa sezione non è stata trovata");
        }
        Subsection subsection = new Subsection();
        subsection.setSubsectionTitle(subsectionDTO.subsectionTitle());
        subsection.setSubsectionDescription(subsectionDTO.subsectionDescription());
        subsection.setSubsectionTime(subsectionDTO.subsectionTime());
        subsection.setSection(section);
        section.getSubsectionList().add(subsection);
        sectionService.update(section);
        return subsectionDAO.save(subsection);
    }

    public void subsectionDelete(Long subsectionId) {
        Subsection delete = this.findById(subsectionId);
        subsectionDAO.delete(delete);
    }

    public Subsection update(Subsection subsection) {
        if (subsectionDAO.existsById(subsection.getSubsectionId())) {
            return subsectionDAO.save(subsection);
        } else {
            throw new IllegalArgumentException("Sottosezione con ID " + subsection.getSubsectionId() + " non esiste");
        }
    }
}
