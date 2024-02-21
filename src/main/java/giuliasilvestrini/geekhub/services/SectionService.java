package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SectionService {
    @Autowired
    private SectionDAO sectionDAO;

    @Autowired
    ConventionService conventionService;
    @Autowired
    Cloudinary cloudinary;

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
        section.setSectionImage("https://i.postimg.cc/RNb0RBRv/placeholder.png");
        section.setConvention(convention);
        section.setCreator(user);

        conventionService.addSectionToConvention(conventionId, section);

        return sectionDAO.save(section);
    }


    public void sectionDelete(Long sectionId, User user) {
        Section section = findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Section not found with ID: " + sectionId);
        }

        Convention convention = section.getConvention();
        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Only ADMIN or the creator of the convention are allowed to delete sections from this convention.");
        }

        sectionDAO.delete(section);
    }


    public Section updateSection(Long sectionId, SectionDTO sectionDTO, User user) {
        Section section = findById(sectionId);

        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId);
        }

        Convention convention = section.getConvention();

        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo l'admin o il creatore di questa convention possono modificare sezioni");
        }

        if (sectionDTO.sectionImage() != null && !sectionDTO.sectionImage().isEmpty()) {
            section.setSectionImage(sectionDTO.sectionImage());
        } else if (section.getSectionImage() == null || section.getSectionImage().isEmpty()) {
            section.setSectionImage("https://i.postimg.cc/RNb0RBRv/placeholder.png");
        }

        section.setSectionTitle(sectionDTO.sectionTitle());
        section.setSectionSubtitle(sectionDTO.sectionSubtitle());

        return sectionDAO.save(section);
    }


    public Section addSubSectionToSection(Long sectionId, Subsection subsection) {
        Section section = findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Convention not found with ID: " + sectionId);
        }
        section.getSubsectionList().add(subsection);
        return sectionDAO.save(section);
    }



    public String uploadSectionImage(MultipartFile file, Long sectionId, User user) throws IOException {
        Section section = findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Convention not found with ID: " + sectionId);
        }
        if (!user.getRole().equals(Role.ADMIN) && !section.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo l admin è il creatore di questa convention possono modificare sezioni");
        }
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        section.setSectionImage(url);
        sectionDAO.save(section);
        return url;
    }

    public Section findBySectionTitle(String sectionTitle) {
        return sectionDAO.findBySectionTitle(sectionTitle).orElseThrow(() -> new NotFoundException("Convention not found with title: " + sectionTitle));
    }
}
