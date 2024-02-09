package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.AccessDeniedException;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.SubsectionDTO;
import giuliasilvestrini.geekhub.services.ConventionService;
import giuliasilvestrini.geekhub.services.SectionService;
import giuliasilvestrini.geekhub.services.SubsectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/conventions/{conventionId}/sec/{sectionId}/")
public class SectionController {

    @Autowired
    SectionService sectionService;

    @Autowired
    ConventionService conventionService;
    @Autowired
    SubsectionService subsectionService;
    private static final Logger logger = LoggerFactory.getLogger(SectionController.class);


    @GetMapping
    public Page<Subsection> getSubsections(@PathVariable long sectionId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "6") int size,
                                           @RequestParam(defaultValue = "subsectionTitle") String order) {


        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId +" " +  section.getSectionTitle());
        }
        return subsectionService.findAll(section, page, size, order);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Subsection saveSubsectionForSection(@PathVariable UUID conventionId, @PathVariable Long sectionId, @RequestBody SubsectionDTO subsectionDTO, @AuthenticationPrincipal User user) {
        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId +" " +  section.getSectionTitle());
        }

        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId + " " + convention.getTitle());
        }
        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della convention sono autorizzati ad aggiungere sottosezioni.");
        }
        return subsectionService.saveSubsection(subsectionDTO, sectionId, user);
    }


    @PutMapping("{subsectionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public Subsection updateSubsection(@PathVariable UUID conventionId, @PathVariable Long sectionId, @PathVariable Long subsectionId, @RequestBody SubsectionDTO subsectionDTO, @AuthenticationPrincipal User user) {
        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId + " " + section.getSectionTitle());
        }
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId + " " + convention.getTitle());
        }
        Subsection subsection = subsectionService.findById(subsectionId);
        if (subsection == null) {
            throw new NotFoundException("Sottosezione non trovata con ID: " + subsectionId + "" + subsection.getSubsectionTitle());
        }
        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della convention sono autorizzati ad aggiornare sottosezioni.");
        }
        return subsectionService.updateSubsection(subsectionId, subsectionDTO, user);
    }

    @DeleteMapping("{subsectionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public void deleteSubsection(@PathVariable UUID conventionId, @PathVariable Long sectionId, @PathVariable Long subsectionId, @AuthenticationPrincipal User user) {
        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId + " " + section.getSectionTitle());
        }

        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId + " " + convention.getTitle());
        }

        Subsection subsection = subsectionService.findById(subsectionId);
        if (subsection == null) {
            throw new NotFoundException("Sottosezione non trovata con ID: " + subsectionId);
        }

        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Solo ADMIN o il creatore della convention sono autorizzati ad eliminare sottosezioni.");
        }

        subsectionService.subsectionDelete(subsectionId, user);
    }
}



