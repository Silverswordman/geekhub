package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import giuliasilvestrini.geekhub.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    UserDAO userDao;
    @Autowired
    Cloudinary cloudinary;

}
