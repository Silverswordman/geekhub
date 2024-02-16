package giuliasilvestrini.geekhub.payloads.authPayload;

import giuliasilvestrini.geekhub.entities.enums.Role;

import java.util.UUID;

public record LoginResponseDTO(String token , Role role, UUID userId) {
}
