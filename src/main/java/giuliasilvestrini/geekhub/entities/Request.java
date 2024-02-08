package giuliasilvestrini.geekhub.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Request {
    @Id
    @GeneratedValue
    private UUID requestId;
    private String email;
    private String name;
    private String message;
    @OneToOne
    @JoinColumn(name = "userId")
    @JsonBackReference
    private User user;

}
