package giuliasilvestrini.geekhub.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Subsection {
    @Id
    @GeneratedValue
    private UUID subsectionId;
    private String subsectionTitle;
    private String subsectionDescription;
    private String subsectionTime;

    @JsonIgnore
    @ManyToOne
    private Section section;
}
