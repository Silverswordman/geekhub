package giuliasilvestrini.geekhub.services;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
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

    public Section findById(UUID sectionId) {
        return sectionDAO.findById(sectionId).orElseThrow(() -> new NotFoundException(sectionId));
    }

    public Section saveSection(SectionDTO sectionDTO, String conventionTitle) {
        Convention convention = conventionService.findByTitle(conventionTitle);
        if (convention == null) {
            throw new NotFoundException("Convention not found with title: " + conventionTitle);
        }
        Section section = new Section();
        section.setSectionTitle(sectionDTO.sectionTitle());
        section.setSectionSubtitle(sectionDTO.sectionSubtitle());
        section.setConvention(convention);
        convention.getSectionList().add(section);
        conventionService.update(convention);
        return sectionDAO.save(section);
    }

    public void sectionDelete(UUID sectionId) {
        Section delete = this.findById(sectionId);
        sectionDAO.delete(delete);
    }

    public Section update(Section section) {
        if (sectionDAO.existsById(section.getSectionId())) {
            return sectionDAO.save(section);
        } else {
            throw new IllegalArgumentException("Section with ID " + section.getSectionId() + " does not exist.");
        }
    }
}
