package giuliasilvestrini.geekhub.payloads;

import jakarta.validation.constraints.NotNull;

public record SubsectionDTO(
        @NotNull(message = "Deve avere un titolo")
        String subsectionTitle,
        @NotNull(message = "Deve avere un corpo del testo")
        String subsectionDescription,
        String subsectionTime,
        @NotNull(message = "Deve avere obbligatoriamente una sezione collegata ")
        String sectionTitle
) {


}
