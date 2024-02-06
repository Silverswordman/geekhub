package giuliasilvestrini.geekhub.controllers;

import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.exceptions.BadRequestException;
import giuliasilvestrini.geekhub.payloads.NewUserDTO;
import giuliasilvestrini.geekhub.payloads.NewUserResponseDTO;
import giuliasilvestrini.geekhub.payloads.authPayload.LoginDTO;
import giuliasilvestrini.geekhub.payloads.authPayload.LoginResponseDTO;
import giuliasilvestrini.geekhub.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        String accessToken = authService.authenticateUser(body);
        return new LoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserResponseDTO createUser(@RequestBody @Validated NewUserDTO newUserPayload, BindingResult validation) {
        if (validation.hasErrors()) {
            System.out.println(validation.hasErrors());
            throw new BadRequestException(validation.getAllErrors());
        } else {
            User newUser = authService.save(newUserPayload);
            return new NewUserResponseDTO(newUser.getUserId());
        }
    }
}