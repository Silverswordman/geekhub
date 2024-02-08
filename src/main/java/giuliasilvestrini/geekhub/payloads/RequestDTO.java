package giuliasilvestrini.geekhub.payloads;



import jakarta.validation.constraints.NotNull;


public record RequestDTO(
        @NotNull String email,
        @NotNull String name,
        @NotNull String message
) {}
