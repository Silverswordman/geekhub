package giuliasilvestrini.geekhub.services;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.AccessDeniedException;
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

    public Subsection saveSubsection(SubsectionDTO subsectionDTO, Long sectionId, User user) {
        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId);
        }
        if (!user.getRole().equals(Role.ADMIN) && !section.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della sezione sono autorizzati ad aggiungere sottosezioni a questa sezione.");
        }
        Subsection subsection = new Subsection();
        subsection.setSubsectionTitle(subsectionDTO.subsectionTitle());
        subsection.setSubsectionDescription(subsectionDTO.subsectionDescription());
        subsection.setSubsectionTime(subsectionDTO.subsectionTime());
        subsection.setSection(section);
        subsection.setCreator(user);
        section.getSubsectionList().add(subsection);
        sectionService.addSubSectionToSection(sectionId,subsection);
        return subsectionDAO.save(subsection);
    }

    public void subsectionDelete(Long subsectionId, User user) {
        Subsection subsection = this.findById(subsectionId);
        Section section = subsection.getSection();

        if (!user.getRole().equals(Role.ADMIN) && !section.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della sezione sono autorizzati ad eliminare sottosezioni da questa sezione.");
        }
        subsectionDAO.delete(subsection);
    }

    public Subsection updateSubsection(Long subsectionId, SubsectionDTO subsectionDTO, User user) {
        Subsection subsection = findById(subsectionId);
        if (subsection == null) {
            throw new NotFoundException("Sottosezione non trovata con ID: " + subsectionId);
        }
        Section section = subsection.getSection();
        if (!user.getRole().equals(Role.ADMIN) && !section.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della sezione sono autorizzati ad aggiornare sottosezioni.");
        }

        subsection.setSubsectionTitle(subsectionDTO.subsectionTitle());
        subsection.setSubsectionDescription(subsectionDTO.subsectionDescription());
        subsection.setSubsectionTime(subsectionDTO.subsectionTime());
        return subsectionDAO.save(subsection);
    }

}
