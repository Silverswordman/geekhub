package giuliasilvestrini.geekhub.payloads.errorsPayload;

import java.time.LocalDateTime;

public record ErrorPayload(String message, LocalDateTime time) {
}
