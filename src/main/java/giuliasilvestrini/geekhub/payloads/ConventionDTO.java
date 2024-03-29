package giuliasilvestrini.geekhub.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConventionDTO(
        @NotNull(message = "la fiera deve avere un titolo")
        String title,
        @NotEmpty(message = "deve avere obbligatoriamente una data di inizio")
        LocalDate startDate,
        @NotEmpty(message = "deve avere obbligatoriamente una data di fine")
        LocalDate endDate,
        String logo,
        String coverImage,
        String site,
        @NotNull(message = "la fiera deve avere un indirizzo")
        String address,

        @NotNull
        String region,
        @NotNull
        String province,
        @NotNull
        String city
) {
}
