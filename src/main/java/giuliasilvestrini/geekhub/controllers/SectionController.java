package giuliasilvestrini.geekhub.controllers;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.Subsection;
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
    public Page<Subsection> getSubsections( @PathVariable long sectionId,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "6") int size,
                                           @RequestParam(defaultValue = "subsectionTitle") String order) {


        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata " + sectionId);
        }
        return subsectionService.findAll(section, page, size, order);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Subsection saveSubectionForSection(@PathVariable long sectionId, @RequestBody SubsectionDTO subsectionDTO) {
        logger.info("Chiamata POST per salvare la subsection per la sezione con ID {}", sectionId);

        // Logga i parametri ricevuti
        logger.info("subsectionDTO: {}", subsectionDTO.toString());

        Section section = sectionService.findById(sectionId);
        if (section == null) {
            throw new NotFoundException("Sezione non trovata con ID: " + sectionId);
        }
        return subsectionService.saveSubsection(subsectionDTO, section.getSectionTitle());
    }
}



