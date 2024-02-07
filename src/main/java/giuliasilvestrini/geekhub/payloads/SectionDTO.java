package giuliasilvestrini.geekhub.payloads;

import jakarta.validation.constraints.NotNull;

public record SectionDTO(
        @NotNull(message = "Deve avere obbligatoriamente un titolo")
        String sectionTitle,
        @NotNull(message = "Deve avere obbligatoriamente un sottotitolo")
        String sectionSubtitle,
        String sectionImage,
        @NotNull(message = "Deve avere obbligatoriamente una fiera collegata ")
        String conventionTitle
) {
}
