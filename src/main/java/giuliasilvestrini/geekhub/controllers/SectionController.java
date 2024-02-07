package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.ConventionDTO;
import giuliasilvestrini.geekhub.payloads.SectionDTO;
import giuliasilvestrini.geekhub.services.ConventionService;
import giuliasilvestrini.geekhub.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sections")
public class SectionController {

//    @Autowired
//    SectionService sectionService;
//
//    @Autowired
//    ConventionService conventionService;
//
//    @GetMapping("/{conventionId}")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public Page<Section> getSections(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "6") int size,
//                                     @RequestParam(defaultValue = "sectionId") String order) {
//
//        Convention convention = conventionService.findById();
//        if (convention == null) {
//            throw new NotFoundException("Convention not found with ID: " + conventionId);
//        }
//        return sectionService.findAll(convention, page, size, order);
//    }
//
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Section saveNewSection(@RequestBody SectionDTO payload) {
//        return sectionService.saveSection(payload, payload.conventionTitle());
//    }
}
