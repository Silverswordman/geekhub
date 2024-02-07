package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDAO userDao;
    @Autowired
    Cloudinary cloudinary;

    public Page<User> findAll(int size, int page, String order){
        Pageable pageable= PageRequest.of(size,page, Sort.by(order));
        return userDao.findAll(pageable);
    }

    public User findById(UUID userId){
        return userDao.findById(userId).orElseThrow(()->new NotFoundException(userId));
    }

    public User userUpdate(UUID userId,User body){
        User update=this.findById(userId);
        update.setName(body.getName());
        update.setSurname(body.getSurname());
        update.setEmail(body.getEmail());
        update.setPassword(body.getPassword());
        update.setAvatar(body.getAvatar());
        update.setRole(body.getRole());
        return userDao.save(update);
    }


    public void userDelete(UUID userId){
        User delete=this.findById(userId);
        userDao.delete(delete);
    }

    public User findByEmail(String email){
        return userDao.findByEmail(email).orElseThrow(()-> new NotFoundException("Utente con email " + email + " non trovato"));
    }

    public  String uploadImage(MultipartFile file, UUID userId) throws IOException {
        User found = this.findById(userId);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(url);
        userDao.save(found);
        return url;
    }
}
