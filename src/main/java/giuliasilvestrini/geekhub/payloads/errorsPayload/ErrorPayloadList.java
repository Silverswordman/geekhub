package giuliasilvestrini.geekhub.payloads.errorsPayload;

import java.util.List;

public record ErrorPayloadList(String message,
                               List<String> errorsList) {
}
