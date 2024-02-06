package giuliasilvestrini.geekhub.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewUserDTO(
        @NotEmpty(message = "username è obbligatorio")
        @Size(min = 3, max = 30, message = "username deve avere minimo 3 caratteri, massimo 30")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il Nome deve avere minimo 3 caratteri, massimo 30")
        String name,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il Cognome deve avere minimo 3 caratteri, massimo 30")
        String surname,
        @Email(message = "L'indirizzo inserito non è un indirizzo valido")
        @NotNull(message = "La mail è un campo obbligatorio!")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 5, max = 30, message = "La password deve avere minimo 5 caratteri, massimo 30")
        String password) {
}
