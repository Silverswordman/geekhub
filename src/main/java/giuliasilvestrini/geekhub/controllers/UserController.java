package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.Request;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.payloads.RequestDTO;
import giuliasilvestrini.geekhub.services.RequestService;
import giuliasilvestrini.geekhub.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    private RequestService requestService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> usersList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "15") int size, @RequestParam(defaultValue = "username") String order) {
        return userService.findAll(page, size, order);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public User uniqueUser(@PathVariable UUID userId) {
        return userService.findById(userId);
    }


    // Edit the user for  the Admin
    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User userUpdate(@PathVariable UUID userId, @RequestBody User body) {
        return userService.userUpdate(userId, body);
    }

    // Delete user for the Admin
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority(ADMIN)")
    public void deleteUser(@PathVariable UUID userId) {
        userService.userDelete(userId);
    }


    // Personal Profile reads the Token

    @GetMapping("/me")
    public User profilePage(@AuthenticationPrincipal User utente) {
        return utente;
    }


    @PutMapping("/me")
    public User updateUser(@AuthenticationPrincipal User userId, @RequestBody User body) {
        return userService.userUpdate(userId.getUserId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void userDelete(@AuthenticationPrincipal User userId) {
        userService.userDelete(userId.getUserId());
    }


    //Upload avatar generale
    @PatchMapping("/{userId}/upload")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String uploadAvatarImg(@RequestParam("image") MultipartFile file, @PathVariable UUID userId) throws Exception {
        return userService.uploadImage(file, userId);
    }

    @PatchMapping("/me/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadAvatarPersonal(@RequestParam("image") MultipartFile file, @AuthenticationPrincipal User userId) throws Exception {
        return userService.uploadImage(file, userId.getUserId());
    }



    // invio richiesta user eventplanner to admin
    @PostMapping("/me/sendRequest")
    @PreAuthorize("hasAuthority('USER')")
    public void sendRequest(@AuthenticationPrincipal User currentUser, @RequestBody RequestDTO requestDTO) {
        String email = currentUser.getEmail();
        String name = currentUser.getName();
        String message = requestDTO.message();

        requestService.saveRequest(email, name, message);
    }

// menu requests solo per admins

    @GetMapping("/requests")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Request> getRequestsPage() {
        return requestService.getRequestsPage(0, 10);
    }

    // Accetta una richiesta
    @PutMapping("/requests/{requestId}/accept")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User acceptRequest(@PathVariable UUID requestId) {
        return requestService.acceptRequest(requestId);
    }

    // Rifiuta una richiesta
    @DeleteMapping("/requests/{requestId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void declineRequest(@PathVariable UUID requestId) {
        requestService.declineRequest(requestId);
    }

}
