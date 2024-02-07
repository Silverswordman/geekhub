package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.ConventionDTO;
import giuliasilvestrini.geekhub.payloads.SectionDTO;
import giuliasilvestrini.geekhub.services.ConventionService;
import giuliasilvestrini.geekhub.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/conventions")
public class ConventionController {
    @Autowired
    ConventionService conventionService;


    @Autowired
    SectionService sectionService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Convention> getConventions(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "conventionId") String order) {
        return conventionService.findAll(page, size, order);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Convention saveNewConvention(@RequestBody ConventionDTO payload) {
        return conventionService.saveConvention(payload);
    }


    @GetMapping("/{conventionId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Convention singleConvention(@PathVariable UUID conventionId) {
        return conventionService.findById(conventionId);
    }

    @PostMapping("/{conventionId}/sections")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Section saveSectionForConvention(@PathVariable UUID conventionId, @RequestBody SectionDTO sectionDTO) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }
        return sectionService.saveSection(sectionDTO, convention.getTitle());
    }


    @GetMapping("/{conventionId}/sections")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Section> getSections(@PathVariable UUID conventionId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(defaultValue = "sectionId") String order) {

        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }
        return sectionService.findAll(convention, page, size, order);
    }



}

