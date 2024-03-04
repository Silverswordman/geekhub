package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.Section;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.AccessDeniedException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/conventions")

public class ConventionController {
    @Autowired
    ConventionService conventionService;


    @Autowired
    SectionService sectionService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)

    public Page<Convention> getConventions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
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


    @PatchMapping("/{conventionId}/uploadLogo")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public String uploadLogo(@RequestParam("image") MultipartFile file, @PathVariable UUID conventionId, @AuthenticationPrincipal User userId) throws Exception {
        return conventionService.uploadLogo(file, conventionId, userId);
    }

    @PatchMapping("/{conventionId}/uploadCover")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public String uploadCover(@RequestParam("image") MultipartFile file, @PathVariable UUID conventionId, @AuthenticationPrincipal User userId) throws Exception {
        return conventionService.uploadCover(file, conventionId, userId);
    }


    @DeleteMapping("/{conventionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public void deleteConvention(@PathVariable UUID conventionId, @AuthenticationPrincipal User user) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention not found with ID: " + conventionId);
        }

        if (!user.getRole().equals(Role.ADMIN) && !convention.getCreator().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Only ADMIN or the creator of the convention are allowed to delete it.");
        }

        conventionService.deleteConvention(conventionId);
    }


    @GetMapping("/{conventionId}/sec")

    public Page<Section> getSections(@PathVariable UUID conventionId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "4") int size,
                                     @RequestParam(defaultValue = "sectionTitle") String order) {

        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId);
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
    public Section saveSectionForConvention(@PathVariable UUID conventionId, @RequestBody SectionDTO sectionDTO, @AuthenticationPrincipal User user) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId);
        }
        return sectionService.saveSection(sectionDTO, convention.getConventionId(), user);
    }


    @DeleteMapping("/{conventionId}/sec/{sectionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public void deleteSection(@PathVariable UUID conventionId, @PathVariable Long sectionId, @AuthenticationPrincipal User user) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId);
        }

        sectionService.sectionDelete(sectionId, user);
    }


    @PutMapping("/{conventionId}/sec/{sectionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public Section updateSection(@PathVariable UUID conventionId, @PathVariable Long sectionId, @RequestBody SectionDTO sectionDTO, @AuthenticationPrincipal User user) {
        Convention convention = conventionService.findById(conventionId);
        if (convention == null) {
            throw new NotFoundException("Convention non trovata " + conventionId);
        }

        Section section = sectionService.updateSection(sectionId, sectionDTO, user);
        return section;
    }


    @PatchMapping("/{conventionId}/sec/{sectionId}/uploadImage")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EVENTPLANNER')")
    public String uploadSectionImage(@RequestParam("image") MultipartFile file, @PathVariable Long sectionId, @AuthenticationPrincipal User userId) throws Exception {
        return sectionService.uploadSectionImage(file, sectionId, userId);
    }

    @GetMapping("/search")
    public List<Convention> searchByTitle(@RequestParam String title) {
        return conventionService.findByTitleContaining(title);
    }

}

