package giuliasilvestrini.geekhub.services;

import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.BadRequestException;
import giuliasilvestrini.geekhub.exceptions.UnauthorizedException;
import giuliasilvestrini.geekhub.payloads.NewUserDTO;
import giuliasilvestrini.geekhub.payloads.authPayload.LoginDTO;
import giuliasilvestrini.geekhub.payloads.authPayload.LoginResponseDTO;
import giuliasilvestrini.geekhub.repositories.UserDAO;
import giuliasilvestrini.geekhub.security.JWTtools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private PasswordEncoder bcrypt;
    @Autowired
    private JWTtools jwTtools;
    @Autowired
    private UserDAO userDao;
    @Autowired
    private UserService userService;

    public LoginResponseDTO authenticateUser(LoginDTO body) {
        User user = userService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            String token = jwTtools.createToken(user);
            return new LoginResponseDTO(token, user.getRole(), user.getUserId());
        } else {
            throw new UnauthorizedException("Credenziali non valide!!");
        }
    }


    public User save(NewUserDTO body) {
        userDao.findByEmail(body.email()).ifPresent(user -> {
            throw new BadRequestException("email " + user.getEmail() + " già in uso!");
        });
        userDao.findByUsername(body.username()).ifPresent(user -> {
            throw new BadRequestException("username " + user.getUsername() + " già in uso!");
        });
        User newUser = new User();
        newUser.setName(body.name());
        newUser.setSurname(body.surname());
        newUser.setUsername(body.username());
        newUser.setEmail(body.email());
        newUser.setPassword(bcrypt.encode(body.password()));
        newUser.setRole(Role.USER);
        newUser.setAvatar("https://ui-avatars.com/api/?name=" + body.name() + "+" + body.surname());
        return userDao.save(newUser);
    }
}