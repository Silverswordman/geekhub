package giuliasilvestrini.geekhub.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import giuliasilvestrini.geekhub.entities.Convention;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.exceptions.BadRequestException;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.repositories.ConventionDAO;
import giuliasilvestrini.geekhub.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    ConventionDAO conventionDAO;
    @Autowired
    Cloudinary cloudinary;

    public Page<User> findAll(int size, int page, String order){
        Pageable pageable= PageRequest.of(size,page, Sort.by(order));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID userId){
        return userDAO.findById(userId).orElseThrow(()->new NotFoundException(userId));
    }

    public User userUpdate(UUID userId,User body){
        User update=this.findById(userId);
        update.setName(body.getName());
        update.setSurname(body.getSurname());
        update.setEmail(body.getEmail());
        update.setPassword(body.getPassword());
        update.setAvatar(body.getAvatar());
        update.setRole(body.getRole());
        return userDAO.save(update);
    }


    public void userDelete(UUID userId){
        User delete=this.findById(userId);
        userDAO.delete(delete);
    }





    public Convention addToFavorites(User currentUser, UUID conventionId) {
        User user = userDAO.findById(currentUser.getUserId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        Convention convention = conventionDAO.findById(conventionId)
                .orElseThrow(() -> new NotFoundException("Convention non trovata"));

        Set<Convention> favoriteConventions = user.getFavoriteConventions();

        if (!favoriteConventions.contains(convention)) {
            favoriteConventions.add(convention);
            user.setFavoriteConventions(favoriteConventions);
            userDAO.save(user);
        } else {
            throw new BadRequestException("La convenzione è già presente nei preferiti dell'utente.");
        }

        return convention;
    }


    public Set<Convention> getFavoriteConventions(User user) {
        return user.getFavoriteConventions();
    }

    public Set<Convention> getFavoriteConventionsByUserId(UUID userId) {
        User user = userDAO.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        return user.getFavoriteConventions();
    }


    public User findByEmail(String email){
        return userDAO.findByEmail(email).orElseThrow(()-> new NotFoundException("Utente con email " + email + " non trovato"));
    }

    public  String uploadImage(MultipartFile file, UUID userId) throws IOException {
        User found = this.findById(userId);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
        found.setAvatar(url);
        userDAO.save(found);
        return url;
    }
}
