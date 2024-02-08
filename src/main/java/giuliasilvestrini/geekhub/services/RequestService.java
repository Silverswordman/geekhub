package giuliasilvestrini.geekhub.services;
import giuliasilvestrini.geekhub.entities.Request;
import giuliasilvestrini.geekhub.entities.User;
import giuliasilvestrini.geekhub.entities.enums.Role;
import giuliasilvestrini.geekhub.exceptions.NotFoundException;
import giuliasilvestrini.geekhub.payloads.RequestDTO;
import giuliasilvestrini.geekhub.repositories.RequestDAO;
import giuliasilvestrini.geekhub.repositories.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RequestService {


    @Autowired
    private RequestDAO requestDAO;

    @Autowired
    private UserDAO userDAO;

    public Page<Request> getRequestsPage(int pageNumber, int pageSize) {
        // Crea un oggetto PageRequest per ottenere una pagina specifica
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        // Ottieni la pagina di richieste utilizzando il metodo findAll del repository
        return requestDAO.findAll(pageRequest);
    }



    public Request saveRequest(String email, String name, String message) {
        User user = userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente non trovato con email: " + email));

        Request request = new Request();
        request.setEmail(email);
        request.setName(name);
        request.setMessage(message);
        request.setUser(user);

        return requestDAO.save(request);
    }



    public User acceptRequest(UUID requestId) {
        Optional<Request> optionalRequest = requestDAO.findById(requestId);
        Request request = optionalRequest.orElseThrow(() -> new NotFoundException("Richiesta non trovata con ID: " + requestId));

        String userEmail = request.getEmail();

        Optional<User> optionalUser = userDAO.findByEmail(userEmail);
        User user = optionalUser.orElseThrow(() -> new NotFoundException("Utente non trovato con email: " + userEmail));

        user.setRole(Role.EVENTPLANNER);
        userDAO.save(user);

        requestDAO.delete(request);

        return user;
    }


    public void declineRequest(UUID requestId) {
        Optional<Request> optionalRequest = requestDAO.findById(requestId);
        Request request = optionalRequest.orElseThrow(() -> new NotFoundException("Richiesta non trovata con ID: " + requestId));

        requestDAO.delete(request);
    }
}