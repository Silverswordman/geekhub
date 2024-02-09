package giuliasilvestrini.geekhub.services;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.AccessDeniedException;
import giuliasilvestrini.geekhub.exceptions.DuplicateEntryException;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.SectionDTO;
import giuliasilvestrini.geekhub.repositories.SectionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SectionService {
    @Autowired
    private SectionDAO sectionDAO;

    @Autowired
    ConventionService conventionService;

    public Page<Section> findAll(Convention convention, int page, int size, String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));
        return sectionDAO.findAllByConvention(convention, pageable);
    }

    public Section findById(Long sectionId) {
        return sectionDAO.findById(sectionId).orElseThrow(() -> new NotFoundException(sectionId));
    }

    public Section saveSection(SectionDTO sectionDTO, UUID conventionId, User user) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId);
        }
        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo l admin è il creatore di questa convention possono aggiungere sezioni");
        }
        Section existingSection = sectionDAO.findBySectionTitleAndConvention(sectionDTO.sectionTitle(), convention);
        if (existingSection != null) {
            throw new DuplicateEntryException("Esiste già una sezione con lo stesso nome");
        }

        Section section = new Section();
        section.setSectionTitle(sectionDTO.sectionTitle());
        section.setSectionSubtitle(sectionDTO.sectionSubtitle());
        section.setSectionImage(sectionDTO.sectionImage());
        section.setConvention(convention);
        section.setCreator(user);

        conventionService.addSectionToConvention(conventionId, section);

        return sectionDAO.save(section);
    }


    public void sectionDelete(Long sectionId) {
        Section delete = this.findById(sectionId);
        sectionDAO.delete(delete);
    }

    public Section update(Section section) {
        if (sectionDAO.existsById(section.getSectionId())) {
            return sectionDAO.save(section);
        } else {
            throw new IllegalArgumentException("Sezione con ID " + section.getSectionId() + " non esiste");
        }
    }

    public Section findBySectionTitle(String sectionTitle) {
        return sectionDAO.findBySectionTitle(sectionTitle).orElseThrow(() -> new NotFoundException("Convention not found with title: " + sectionTitle));
    }
}
