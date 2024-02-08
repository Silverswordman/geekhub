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

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Page<Convention> getConventions(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "conventionId") String order) {
        return conventionService.findAll(page, size, order);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public Convention saveNewConvention(@RequestBody ConventionDTO payload, @AuthenticationPrincipal User user) {
        return conventionService.saveConvention(payload, user);
    }




    @PutMapping("/{conventionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public Convention updateConvention(@PathVariable UUID conventionId, @RequestBody ConventionDTO conventionDTO, @AuthenticationPrincipal User user) {
        System.out.println("User UUID: " + user.getUserId());
        System.out.println("Creator UUID: " + conventionService.findById(conventionId).getCreator().getUserId());
        return conventionService.updateConvention(conventionId, conventionDTO, user);
    }
    @GetMapping("/{conventionId}")

    @ResponseStatus(HttpStatus.OK)
    public Convention singleConvention(@PathVariable UUID conventionId) {
        return conventionService.findById(conventionId);
    }


    @GetMapping("/{conventionId}/sec")

    public Page<Section> getSections(@PathVariable UUID conventionId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "6") int size,
                                     @RequestParam(defaultValue = "sectionTitle") String order) {

        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }
        return sectionService.findAll(convention, page, size, order);
    }


    @GetMapping("/{conventionId}/sec/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public Section singleSection(@PathVariable Long sectionId) {
        return sectionService.findById(sectionId);
    }


    @PostMapping("/{conventionId}/sec")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    @ResponseStatus(HttpStatus.CREATED)
    public Section saveSectionForConvention(@PathVariable UUID conventionId, @RequestBody SectionDTO sectionDTO) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }
        return sectionService.saveSection(sectionDTO, convention.getTitle());
    }



}

